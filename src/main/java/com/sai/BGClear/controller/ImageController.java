package com.sai.BGClear.controller;


import com.sai.BGClear.dto.UserDTO;
import com.sai.BGClear.response.ClearBgResponse;
import com.sai.BGClear.service.RemoveBackgroundService;
import com.sai.BGClear.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final RemoveBackgroundService removeBackgroundService;
    private final UserService userService;

    @PostMapping("/remove-background")
    public ResponseEntity<?> removeBackground(@RequestParam("file")MultipartFile file, Authentication authentication){

        ClearBgResponse response = null;
        Map<String , Object> responseMap = new HashMap<>();
        try{
            // validation for authentication
            if(authentication.getName().isEmpty() || authentication.getName() == null){
                response = ClearBgResponse.builder()
                        .statusCode(HttpStatus.FORBIDDEN)
                        .success(false)
                        .data("You are not authorized to access this resource")
                        .build();
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            UserDTO userDTO = userService.getUserByClerkId(authentication.getName());

            // validation for user credits
            if(userDTO.getCredits() == 0){
                responseMap.put("message", "You have no credits left. Please purchase more credits to continue using the service.");
                responseMap.put("creditBalance", userDTO.getCredits());
                response = ClearBgResponse.builder()
                        .statusCode(HttpStatus.OK)
                        .success(false)
                        .data(responseMap)
                        .build();
                return ResponseEntity.ok(response);
            }


            byte[] imageBytes = removeBackgroundService.removeBackground(file);
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            userDTO.setCredits(userDTO.getCredits() - 1);
            userService.saveUser(userDTO);

            return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(base64Image);

        }catch (Exception e){
            response = ClearBgResponse.builder()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .success(false)
                    .data("Something went wrong. Please try again later.")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
