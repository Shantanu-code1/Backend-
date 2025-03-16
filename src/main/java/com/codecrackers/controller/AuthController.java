package com.codecrackers.controller;

import com.codecrackers.config.JwtProvider;
import com.codecrackers.model.Student;
import com.codecrackers.model.USER_ROLE;
import com.codecrackers.repository.StudentRepository;
import com.codecrackers.request.SignInRequest;
import com.codecrackers.request.SingUpRequest;
import com.codecrackers.response.AuthResponse;
import com.codecrackers.response.OtpResponse;
import com.codecrackers.service.CustomUserDetailsService;
import com.codecrackers.service.EmailService;
import com.codecrackers.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Random;

@Slf4j
@RestController
@RequestMapping("/code")
public class AuthController {
    @Autowired
    private EmailService emailService;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentService studentService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @PostMapping("/signup")
    private ResponseEntity<AuthResponse> signUp(@RequestParam(name = "ref", required = false) String referralCode, @RequestBody SingUpRequest singUpRequest) throws Exception {

        System.out.println("singUpRequest "+ singUpRequest);
        Student isEmailExitStudent = studentRepository.findByEmail(singUpRequest.getEmail());
        
        if(isEmailExitStudent != null){
            System.out.println("email exist already.....");
            if(isEmailExitStudent.isVerify()){
                throw new Exception("User already exist with this email: " + isEmailExitStudent.getEmail());
            }
            else{
                System.out.println("not verified");
                String otp = generateOTP();
                System.out.println("not verified otp" + otp + " " + isEmailExitStudent.getEmail());
                isEmailExitStudent.setOtp(otp);
                if(referralCode != null) {
                    studentService.increasePointOnShare(referralCode);
                }
                studentRepository.save(isEmailExitStudent);
                sendVerificationEmail(isEmailExitStudent.getEmail(), otp);
                return new ResponseEntity<>(new AuthResponse(null, false, "Verification email sent again", null), HttpStatus.OK);
            }
        }

        Student student = new Student();
        student.setName(singUpRequest.getName());
        student.setEmail(singUpRequest.getEmail());
        student.setRole(singUpRequest.getRole());
        student.setPassword(passwordEncoder.encode(singUpRequest.getPassword()));
        student.setPhoneNumber(singUpRequest.getPhoneNumber());
        String otp = generateOTP();

        student.setOtp(otp);

        if(referralCode != null) {
            studentService.increasePointOnShare(referralCode);
        }
        studentRepository.save(student);
        System.out.println("Checking OTP " + otp);
        sendVerificationEmail(singUpRequest.getEmail(), otp);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(student.getEmail(), student.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setStatus(true);
        authResponse.setMessage("Register Successfully");
        authResponse.setRole(student.getRole());

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    private ResponseEntity<AuthResponse> signIn(@RequestBody SignInRequest signInRequest){
        String username = signInRequest.getEmail();
        String password = signInRequest.getPassword();

        UsernamePasswordAuthenticationToken authentication = authenticate(username, password);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.isEmpty()? null : authorities.iterator().next().getAuthority();

        String jwt = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Login Successfully");
        authResponse.setRole(USER_ROLE.valueOf(role));
        authResponse.setStatus(true);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PostMapping("/verify")
    public ResponseEntity<OtpResponse> verifyUser(@RequestParam String email, @RequestParam String otp) {
        OtpResponse otpResponse = new OtpResponse();
        try {
            verify(email, otp);
            otpResponse.setStatus("true");
            otpResponse.setMessage("User verified successfully");

            Student user = studentRepository.findByEmail(email);
            otpResponse.setStudent(user);

            return new ResponseEntity<>(otpResponse, HttpStatus.OK);
        } catch (RuntimeException e) {
            otpResponse.setStatus("false");
            otpResponse.setMessage("Verification failed: " + e.getMessage());
            return new ResponseEntity<>(otpResponse, HttpStatus.BAD_REQUEST);
        }
    }

    public void verify(String email, String otp) {
        Student user = studentRepository.findByEmail(email);
        if(user == null){
            throw new RuntimeException("user not found");
        }else if (user.isVerify()) {
            throw new RuntimeException("User is already verified");
        }else if(otp.equals(user.getOtp())){
            user.setVerify(true);
            studentRepository.save(user);
        } else{
            throw new RuntimeException("Internal server error");
        }
    }

    private String generateOTP(){
        Random random = new Random();
        int otpValue = 100000 + random.nextInt(900000);
        return String.valueOf(otpValue);
    }

    private void sendVerificationEmail(String email, String otp){
        String subject = "Email verification";
        String body = "Your verification otp is: " + otp;
        emailService.sendEmail(email, subject, body);
    }

    private UsernamePasswordAuthenticationToken authenticate(String username, String password) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        if(userDetails == null){
            throw new BadCredentialsException("Invalid username....");
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            System.out.println("Entered Password: " + password);
            System.out.println("Stored Hashed Password: " + userDetails.getPassword());

            throw new BadCredentialsException("Invalid Password...");
        }

        Student user = studentRepository.findByEmail(username);
        if (user == null) {
            throw new BadCredentialsException("User not found.");
        }

        if (!user.isVerify()) {
            throw new BadCredentialsException("User is not verified.");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
