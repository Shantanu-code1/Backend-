package com.codecrackers.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Table(name = "DOUBTS")
public class Doubt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Title field for the question title
    private String title;
    
    // Topic field is used as category
    private String topic;
    
    // Description for detailed explanation
    @Column(length = 2000)
    private String description;
    
    // The doubt field could be repurposed or used alongside title
    private String doubt;
    
    // Code snippet (optional)
    @Column(length = 5000)
    private String codeSnippet;
    
    // Tags as a comma-separated string
    private String tags;
    
    // Type field to distinguish between doubts and queries
    @Enumerated(EnumType.STRING)
    private DoubtType type = DoubtType.DOUBT;
    
    private IsSolvedDoubt isSolved = IsSolvedDoubt.PENDING;
    private String doubtImage;

    @ManyToOne
    @JsonBackReference
    private Student student;

    private String timeSubmitted;
    
    // Helper method to get tags as a list
    @Transient
    public List<String> getTagsList() {
        if (tags == null || tags.isEmpty()) {
            return new ArrayList<>();
        }
        
        String[] tagArray = tags.split(",");
        List<String> tagList = new ArrayList<>();
        for (String tag : tagArray) {
            tagList.add(tag.trim());
        }
        return tagList;
    }
    
    // Helper method to set tags from a list
    public void setTagsFromList(List<String> tagList) {
        if (tagList == null || tagList.isEmpty()) {
            this.tags = "";
            return;
        }
        
        this.tags = String.join(",", tagList);
    }
}
