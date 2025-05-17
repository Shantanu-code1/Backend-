package com.codecrackers.service;

import com.codecrackers.dto.RecentDoubtDTO;
import com.codecrackers.model.Doubt;
import com.codecrackers.model.DoubtType;
import com.codecrackers.model.IsSolvedDoubt;
import com.codecrackers.repository.DoubtRepository;
import com.codecrackers.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.codecrackers.model.Student;
import com.codecrackers.config.JwtProvider;
import com.codecrackers.repository.StudentRepository;

import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class DoubtServiceImpl implements DoubtService {
    
    @Autowired
    private DoubtRepository doubtRepository;
    
    @Autowired
    private StudentService studentService;

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private StudentRepository studentRepository;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER_SUBMITTED = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mma");
    private static final int HOURS_THRESHOLD_FOR_NEW = 24; // Doubts less than 24 hours old are considered new
    
    @Override
    public List<RecentDoubtDTO> getRecentDoubts(int limit) {
        // Only get items of type DOUBT, not QUERY
        List<Doubt> recentDoubts = doubtRepository.findRecentDoubtsByTypeWithLimit(DoubtType.DOUBT.name(), limit);
        return convertToRecentDoubtDTOs(recentDoubts);
    }
    
    @Override
    public List<RecentDoubtDTO> getRecentDoubtsByCategory(String category, int limit) {
        // Only get items of type DOUBT, not QUERY
        List<Doubt> categoryDoubts = doubtRepository.findRecentDoubtsByCategoryAndType(category, DoubtType.DOUBT);
        
        // Apply limit manually if not using a native query with LIMIT
        if (categoryDoubts.size() > limit) {
            categoryDoubts = categoryDoubts.subList(0, limit);
        }
        
        return convertToRecentDoubtDTOs(categoryDoubts);
    }
    
    @Override
    public List<RecentDoubtDTO> getRecentDoubtsByFilter(String filter, int limit) {
        List<Doubt> filteredDoubts;
        
        switch (filter.toLowerCase()) {
            case "doubts":
                filteredDoubts = doubtRepository.findRecentDoubtsByTypeWithLimit(DoubtType.DOUBT.name(), limit);
                break;
            case "queries":
                filteredDoubts = doubtRepository.findRecentDoubtsByTypeWithLimit(DoubtType.QUERY.name(), limit);
                break;
            case "ai":
                // For AI-related doubts or other custom filters 
                // This is a placeholder - you might want to add a specific field for AI-related items
                filteredDoubts = doubtRepository.findRecentDoubtsWithLimit(limit);
                break;
            default:
                filteredDoubts = doubtRepository.findRecentDoubtsWithLimit(limit);
        }
        
        // Apply limit manually if needed
        if (filteredDoubts.size() > limit) {
            filteredDoubts = filteredDoubts.subList(0, limit);
        }
        
        return convertToRecentDoubtDTOs(filteredDoubts);
    }
    
    @Override
    public List<Doubt> getAllDoubts() {
        return doubtRepository.findByType(DoubtType.DOUBT);
    }
    
    @Override
    public List<Doubt> getDoubtsByUserId(Long userId) {
        // In this case, get both doubts and queries for the user history
        return doubtRepository.findByStudentId(userId);
    }
    
    @Override
    public List<Doubt> getDoubtsByUserEmail(String email) {
        // In this case, get both doubts and queries for the user history
        return doubtRepository.findByStudentEmail(email);
    }
    
    private boolean isRecentlyCreated(String timeSubmitted) {
        if (timeSubmitted == null) {
            return false;
        }
        
        try {
            LocalDateTime submittedTime = LocalDateTime.parse(timeSubmitted, DATE_FORMATTER);
            LocalDateTime hoursAgo = LocalDateTime.now().minusHours(HOURS_THRESHOLD_FOR_NEW);
            
            return submittedTime.isAfter(hoursAgo);
        } catch (Exception e) {
            return false;
        }
    }
    
    private List<RecentDoubtDTO> convertToRecentDoubtDTOs(List<Doubt> doubts) {
        List<RecentDoubtDTO> dtoList = new ArrayList<>();
        
        for (Doubt doubt : doubts) {
            RecentDoubtDTO dto = new RecentDoubtDTO();
            dto.setId(doubt.getId());
            
            // Set the title (using doubt or topic field)
            dto.setTitle(doubt.getDoubt() != null ? doubt.getDoubt() : "Untitled Doubt");
            
            // Set category from topic field
            dto.setCategory(doubt.getTopic() != null ? doubt.getTopic() : "General");
            
            // Format the time as a human-readable relative time
            dto.setTime(TimeUtil.getRelativeTimeFromNow(doubt.getTimeSubmitted()));
            
            // Set status based on IsSolvedDoubt enum
            dto.setStatus(mapSolvedStatus(doubt.getIsSolved()));
            
            // Use a random placeholder value for replies until the doubt_replies table is created
            // Later you can replace this with the actual implementation
            // dto.setReplies(doubtRepository.countRepliesForDoubt(doubt.getId()));
            dto.setReplies((int) (Math.random() * 5)); // Random number of replies between 0-4
            
            // Check if doubt is new (less than 24 hours old)
            dto.setNew(isRecentlyCreated(doubt.getTimeSubmitted()));
            
            dtoList.add(dto);
        }
        
        return dtoList;
    }
    
    private String mapSolvedStatus(IsSolvedDoubt status) {
        if (status == null) {
            return "Pending";
        }
        
        switch (status) {
            case DONE:
                return "Answered";
            case IN_PROGRESS:
                return "In Progress";
            case PENDING:
            default:
                return "Pending";
        }
    }

    @Override
    public void initiateAnswerSession(Long doubtId, String initiatorJwtToken) throws Exception {
        Doubt doubt = doubtRepository.findById(doubtId)
                .orElseThrow(() -> new RuntimeException("Doubt not found with ID: " + doubtId));

        Student studentToNotify = doubt.getStudent();
        if (studentToNotify == null || studentToNotify.getEmail() == null) {
            throw new RuntimeException("Student details not found or email is missing for the doubt.");
        }

        Student initiator = studentService.findUserByJWT(initiatorJwtToken);
        if (initiator == null) {
            throw new RuntimeException("Initiator (teacher/user) not found.");
        }

        String studentEmail = studentToNotify.getEmail();
        String studentName = studentToNotify.getName() != null ? studentToNotify.getName() : "Student";
        String initiatorName = initiator.getName() != null ? initiator.getName() : "An expert";
        
        String doubtTitle = doubt.getTitle() != null ? doubt.getTitle() : "Your Doubt";
        String doubtDescription = doubt.getDescription() != null ? doubt.getDescription() : "N/A";
        
        String doubtTimeSubmittedFormatted = "N/A";
        if (doubt.getTimeSubmitted() != null) {
            try {
                LocalDateTime submittedTime = LocalDateTime.parse(doubt.getTimeSubmitted(), DateTimeFormatter.ISO_DATE_TIME);
                doubtTimeSubmittedFormatted = submittedTime.format(DATE_FORMATTER_SUBMITTED);
            } catch (Exception e) {
                // Fallback if parsing fails, though ISO_DATE_TIME should be robust
                doubtTimeSubmittedFormatted = doubt.getTimeSubmitted(); 
            }
        }
        
        String googleMeetLink = "https://meet.google.com/new";
        String currentYear = String.valueOf(Year.now().getValue());

        String emailSubject = "Answer Session for Your Doubt: " + doubtTitle;
        
        String htmlEmailBody = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Doubt Answer Session</title>
                <style>
                    body { font-family: 'Arial', sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; color: #333; }
                    .container { max-width: 600px; margin: 20px auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 0 15px rgba(0,0,0,0.1); border-left: 5px solid #007bff; }
                    .header { text-align: center; padding-bottom: 20px; border-bottom: 1px solid #eeeeee; }
                    .header h1 { color: #007bff; margin: 0; font-size: 24px; }
                    .content { padding: 20px 0; line-height: 1.6; }
                    .content h2 { color: #333; font-size: 18px; margin-top: 0; }
                    .content p { margin: 10px 0; }
                    .doubt-details { background-color: #f9f9f9; padding: 15px; border-radius: 5px; margin-bottom: 20px; border: 1px solid #e9e9e9; }
                    .doubt-details strong { display: inline-block; min-width: 100px; }
                    .cta-button { display: block; width: fit-content; margin: 20px auto; padding: 12px 25px; background-color: #007bff; color: #ffffff !important; text-decoration: none; border-radius: 5px; font-size: 16px; text-align: center; }
                    .footer { text-align: center; padding-top: 20px; border-top: 1px solid #eeeeee; font-size: 12px; color: #777; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header"><h1>Doubt Answer Session Initiated</h1></div>
                    <div class="content">
                        <p>Hello %s,</p>
                        <p>An expert, <strong>%s</strong>, is ready to help you with your doubt. An online session is being initiated!</p>
                        <h2>Doubt Details:</h2>
                        <div class="doubt-details">
                            <p><strong>Title:</strong> %s</p>
                            <p><strong>Description:</strong></p>
                            <p>%s</p>
                            <p><strong>Submitted At:</strong> %s</p>
                        </div>
                        <p>Please join the Google Meet session using the link below. The session will begin once <strong>%s</strong> joins the meeting.</p>
                        <a href="%s" class="cta-button" style="color: #ffffff;">Join Google Meet Session</a>
                        <p style="text-align:center; font-size:12px; color:#555;">If the button doesn't work, copy and paste this link into your browser: <br/> %s</p>
                    </div>
                    <div class="footer">
                        <p>&copy; %s CodeCrackers. All rights reserved.</p>
                        <p>If you did not request this, please ignore this email.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(
            studentName,
            initiatorName,
            doubtTitle,
            doubtDescription,
            doubtTimeSubmittedFormatted,
            initiatorName,
            googleMeetLink,
            googleMeetLink,
            currentYear
        );

        // CRITICAL: Ensure your EmailService is set up to send HTML emails.
        // This often involves setting the content type to "text/html".
        // Example (conceptual, actual implementation depends on your EmailService):
        // emailService.sendHtmlEmail(studentEmail, emailSubject, htmlEmailBody); 
        // OR if your sendEmail can take a boolean for html:
        // emailService.sendEmail(studentEmail, emailSubject, htmlEmailBody, true);
        emailService.sendEmail(studentEmail, emailSubject, htmlEmailBody); // Assuming your sendEmail handles HTML if the body is HTML
    }
} 