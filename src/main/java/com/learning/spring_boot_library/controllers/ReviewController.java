package com.learning.spring_boot_library.controllers;

import com.learning.spring_boot_library.models.request.ReviewRequest;
import com.learning.spring_boot_library.services.ReviewService;
import com.learning.spring_boot_library.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/secure")
    public void postReview(@RequestHeader(value = "Authorization") String token,
                           @RequestBody ReviewRequest reviewRequest) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        if (userEmail == null){
            throw new Exception("User email is missing");
        }

        reviewService.postReview(userEmail, reviewRequest);
    }

    @GetMapping("/secure/user/book")
    public Boolean reviewBookByUser(@RequestHeader(value = "Authorization") String token,
                                    @RequestParam Long bookId
    ) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        if (userEmail == null){
            throw new Exception("User email is missing");
        }

        return reviewService.userReviewListed(userEmail, bookId);
    }
}
