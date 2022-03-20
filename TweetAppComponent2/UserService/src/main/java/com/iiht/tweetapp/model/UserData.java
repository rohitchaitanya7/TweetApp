package com.iiht.tweetapp.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Table(value="user_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserData {
	
	    @Column
	    @Schema(description = "First name of the user",required = true,example="kumar")
	    @NotBlank(message="First name should not be empty")
	    private String firstName;
	    @Column
	    @Schema(description = "Last name of the user",required = true,example="P R")
	    @NotBlank(message="Last name should not be empty")
	    private String lastName;
	    @PrimaryKey
	    @Schema(description = "user name of the user",required = true,example="kumar@gmail.com")
	    @Pattern(regexp = "[a-zA-Z0-9@.]*$", message = "user name should contain only alphabets and digits")
	    private String userName;
	    @Column
	    @Schema(description = "Password of the user",required = true,example="Kumar123")
	    @NotBlank(message="Password should not be empty")
	    @Size(min = 8, message = "minimum 8 Characters required")
	    private String password;
	    @Column
	    @Schema(description = "Contact Number of the user",required = true,example="9898989898")
	    private long contactNo;
	    
}
