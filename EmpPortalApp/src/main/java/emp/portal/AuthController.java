package emp.portal;

import java.util.Collections;

import javax.validation.Valid;

import emp.portal.payload.LoginDto;
import emp.portal.payload.SignUpDto;
import emp.portal.repository.RoleRepository;
import emp.portal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
	@Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
	private JwtUtil jwtTokenUtil;
    
    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    private static final Gson gson =new Gson();
    
    @RequestMapping({ "/hello" })
	public String firstPage() {
		return "Hello World";
	}


    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser(@RequestBody LoginDto loginDto) {
    	
        try{org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
       // return new ResponseEntity<>("User signed-in successfully!.", HttpStatus.OK);
        
        UserDetails userDetails = userDetailsService
				.loadUserByUsername(loginDto.getEmail());

		final String jwt = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new AuthenticationResponse(jwt));}catch(Exception e){
            return ResponseHandler.generateresponse("Email or Password Incorrect",HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignUpDto signUpDto){

        // add check for email exists in DB
        if(userRepository.existsByEmail(signUpDto.getEmail())){
           // return new ResponseEntity<>(gson.toJson("Email is already taken"),HttpStatus.BAD_REQUEST);
        	return ResponseHandler.generateresponse("Email is already taken",HttpStatus.BAD_REQUEST);
        }

        // create user object
        User user = new User();

        if(!(signUpDto.getName() == null)) {
            if (!(signUpDto.getName().isEmpty())) {
                if(!(signUpDto.getName().trim().isEmpty())) {

                    user.setName(signUpDto.getName());
                }
                else {
                    return ResponseHandler.validateresponse("Name Field is Required", "name", HttpStatus.BAD_REQUEST);
                }
            }
            else {
                return ResponseHandler.validateresponse("Name Field is Required", "name", HttpStatus.BAD_REQUEST);
            }
        }
        else {
            return ResponseHandler.validateresponse("Name Field is Required", "name", HttpStatus.BAD_REQUEST);
            }
        if(!(signUpDto.getEmail() == null)) {
            if (!(signUpDto.getEmail().isEmpty())) {
                if(!(signUpDto.getEmail().trim().isEmpty())) {

                    user.setEmail(signUpDto.getEmail());
                }
                else {
                    return ResponseHandler.validateresponse("Email Field is Required", "email", HttpStatus.BAD_REQUEST);
                }
            }
            else {
                return ResponseHandler.validateresponse("Email Field is Required", "email", HttpStatus.BAD_REQUEST);
            }
        }
        else {
            return ResponseHandler.validateresponse("Email Field is Required", "email", HttpStatus.BAD_REQUEST);
        }
        if(signUpDto.getPassword() == null) {
            return ResponseHandler.validateresponse("Password Field is Required", "password", HttpStatus.BAD_REQUEST);
        }
        if (signUpDto.getPassword().isEmpty()) {
            return ResponseHandler.validateresponse("Password Field is Required", "password", HttpStatus.BAD_REQUEST);
        }

        if(signUpDto.getPassword().trim().isEmpty()) {

            return ResponseHandler.validateresponse("Password Field is Required", "password", HttpStatus.BAD_REQUEST);
        }



       // user.setEmail(signUpDto.getEmail());

        int maxlength=30,minlength=8,hasupper=0,haslower=0;
        int special=0,digits=0,space=0;
        char ch;

        int size=signUpDto.getPassword().length();

        if(size<minlength || size>maxlength)
        {
            return ResponseHandler.validateresponse("Password Length has to be between 8 and 30", "password", HttpStatus.BAD_REQUEST);
        }
        else
        {
            for (int i=0;i<size;i++)
            {
                ch=signUpDto.getPassword().charAt(i);
                if(Character.isUpperCase(ch))
                    hasupper++;
                else if (Character.isLowerCase(ch))
                    haslower++;
                else if(Character.isDigit(ch))
                    digits++;
                else if (Character.isSpaceChar(ch))
                    space++;
                else
                {
                    special++;
                }


            }

        }
        if(hasupper!=0 && haslower!=0 && digits!=0 && special!=0 && space==0)
        {
            if(signUpDto.getPassword().equals(signUpDto.getConfirmPassword())) {
                user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
            }
            else
            {
                return ResponseHandler.generateresponse("Password and Confirm Password Are Not Same",HttpStatus.BAD_REQUEST);
            }
        }
        else
        {
            if (hasupper==0)
            return ResponseHandler.validateresponse("Password must contain at least one upper case character", "password", HttpStatus.BAD_REQUEST);
            if (haslower==0)
            return ResponseHandler.validateresponse("Password must contain at least one lower case character", "password", HttpStatus.BAD_REQUEST);
            if (digits==0)
            return ResponseHandler.validateresponse("Password must contain at least one digit ", "password", HttpStatus.BAD_REQUEST);
            if (special==0)
            return ResponseHandler.validateresponse("Password must contain at least one special character", "password", HttpStatus.BAD_REQUEST);
            if (space!=0)
                return ResponseHandler.validateresponse("Password Should Not Contain Any Space", "password", HttpStatus.BAD_REQUEST);
        }


        if((signUpDto.getConfirmPassword() == null))
        {
           return  ResponseHandler.validateresponse("Confirm Password Field is Required", "confirmPassword", HttpStatus.BAD_REQUEST);
        }

        if(signUpDto.getPassword().equals(signUpDto.getConfirmPassword())) {
            user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        }
        else
        {
            return ResponseHandler.generateresponse("Password and Confirm Password Are Not Same",HttpStatus.BAD_REQUEST);
        }

        Role roles = roleRepository.findByName("ROLE_ADMIN").get();
        user.setRoles(Collections.singleton(roles));

        userRepository.save(user);
       // return new ResponseEntity<>(gson.toJson("User registered successfully"),HttpStatus.OK);
        return ResponseHandler.generateresponse("User registered successfully",HttpStatus.OK);
    }
}
