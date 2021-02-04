Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('02fb1cb5-138c-11eb-9b1e-f48e38ab9348', 'jws-welcome', '<div>
	<h1>Welcome to <@resourceBundleWithDefault "projectName" "JQuiver"/></h1>
	
		<#if loggedInUser == true >
			Welcome ${userName}
			<a href="tsms/users">Users</a>
			<a href="tsms/destinations">Destinations</a>
		<#else>
			<a href="/cf/register">Register here</a>
			<a href="/cf/login">Login here</a>
		</#if>

	
	
</div>

','aar.dev@trigyn.com','aar.dev@trigyn.com',now(), 2);



Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('0c7acdf9-138c-11eb-9b1e-f48e38ab9348', 'jws-login', ' 
 
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Please sign in</title>
   
   	<@templateWithoutParams "jws-common-css-js"/>
        <script>
           
            function showHidePassword(thisObj){
                var element = $(thisObj).parent().find("input[name=''password'']");
                if (element.prop("type") === "password") {
                    element.prop("type","text");
                    $(thisObj).find("i").removeClass("fa-eye");
                     $(thisObj).find("i").addClass("fa-eye-slash");
                     $("#password").focus();
                } else {
                     element.prop("type","password");
                     $(thisObj).find("i").removeClass("fa-eye-slash");
                     $(thisObj).find("i").addClass("fa-eye");
                     $("#password").focus();
                }

            }
             $(function(){
                $("#reloadCaptcha").click(function(event){
                	$("#imgCaptcha").attr("src", $("#imgCaptcha").attr("src")+"#");
           		});
          	});
        </script>
  </head>
<body>
<div class="container">

    <div class="row">
        <div class="col-7">
            <div class="loginbg"><img src="/webjars/1.0/images/LoginBg.jpg"></div> 
        </div> 

        <div class="col-5">
             <h2 class="form-signin-heading text-center">Welcome To <span class="cm-logotext"><@resourceBundleWithDefault "projectName" "JQuiver"/></span>	
                </h2>	
                <#if authenticationType == "2"> 	
                <div>	
                    <form class="form-signin" method="post" action="/cf/login" autocomplete="off">	
                	
                    <#if queryString?? && queryString == "error">	
                        <#if exceptionMessage?? >	
                            <div class="alert alert-danger" role="alert">${exceptionMessage}</div>	
                        <#else>	
                            <div class="alert alert-danger" role="alert">Bad Credentials</div>	
                        </#if>	
                    <#elseif queryString?? &&  queryString == "logout">	
                        <div class="alert alert-success" role="alert">You have been signed out</div> 	
                    </#if>	
                    <#if resetPasswordSuccess??>	
                        <div class="alert alert-success" role="alert">${resetPasswordSuccess} </div>	
                    </#if>	
                    <p class="divdeform">	
                        <label for="username" class="formlablename">Email</label>	
                        <span class="formicosn"><i class="fa fa-user" aria-hidden="true"></i></span>	
                        <input type="email" id="email" name="email" class="form-control" placeholder="Enter Your Email" required autofocus>	
                    </p>	
                        <#if !enableGoogleAuthenticator >	
                        <p class="divdeform"> 	
                            <label for="password" class="formlablename">Password</label>	
                            <span class="formicosn"><i class="fa fa-unlock-alt" aria-hidden="true"></i></span>	
                            <input type="password" id="password" name="password" class="form-control" placeholder="Enter Your Password" required>	
                            <span class="passview" onclick="showHidePassword(this);"><i class="fa fa-eye" aria-hidden="true"></i></span>	
                        </p>	
                        <#if enableCaptcha >	
                            <p>	
                                <img id="imgCaptcha" name="imgCaptcha" src="/cf/captcha/loginCaptcha">	
                                <span id="reloadCaptcha"><i class="fa fa-refresh" aria-hidden="true"></i></span>	
                                <label for="captcha" class="sr-only">Enter Captcha</label>	
                                <input type="text" id="captcha" name="captcha" class="form-control" placeholder="Enter Captcha" required autofocus >	
                            </p>	
                        </#if> 	
                        <span class="remebermeblock">	
                            <input type="checkbox" name="remember-me" id="remember-me">   <label for="remember-me">Remember Me</label>	
                        </span>    	
                        <span class="forgotpassword">	
                            <a href="/cf/resetPasswordPage">Forgot password?</a> 	
                        </span>	
                    <#else>	
                        <p class="divdeform">	
                            <label for="password" class="formlablename">Enter TOTP</label>	
                            <input type="text" id="password" name="password" class="form-control" placeholder="Enter TOTP" required autofocus >	
                        	
                        </p>	
                        <span class="remebermeblock">	
                            <input type="checkbox" name="remember-me" id="remember-me">   <label for="remember-me">Remeber Me</label>	
                        </span>    	
                        <span class="forgotpassword">	
                            <a href="/cf/configureTOTP">Not Configured? Click here</a> 	
                        </span>	
                        	
                    </#if>   	
                	
                <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>	
                    <#if enableRegistration?? && enableRegistration?string("yes", "no") == "yes" >	
                        <p class="registerlink">New User?	
                            <a href="/cf/register"> Click here to register</a>	
                        </p>	
                    </#if>	
                </form>	
                </div>	
                <#elseif authenticationType == "4">	
                <#if client?? && client == "office365" >	
                <div class="block-wrap">	
                    <div>	
		                <a class="btn-microsoft" href="/oauth2/authorization/office365">	
                            <div class="ms-content">	
                                <div class="logo">	
                                    <img src="/webjars/1.0/images/jc-microsoft.svg">	
                                </div>	
                                <p>Sign in with Microsoft</p>	
                            </div>	
		                </a>	
                    </div>	
                <#elseif client?? && client == "google" >	
                <div class="block-wrap">	
                    <div>	
                    <a class="btn-google" href="/oauth2/authorization/google">	
                        <div class="google-content">	
                            <div class="logo">	
                                <img src="/webjars/1.0/images/jc-google.svg">	
                            </div>	
                            <p>Sign in with Google</p>	
                        </div>	
                    </a>	
                    </div>	
                </div>	
                <#elseif client?? && client == "facebook" >	
                <div class="block-wrap">	
                    <div>	
                        <a class="btn-fb" href="/oauth2/authorization/facebook">	
                            <div class="fb-content">	
                                <div class="logo">	
                                    <img src="/webjars/1.0/images/jc-facebook.svg">	
                                </div>	
                                <p>Sign in with Facebook</p>	
                            </div>	
                        </a>	
                    </div>	
                </div>	
                </#if>
             </div>
            </#if>   
                
        </div>
    </div>   
</div> 

</body></html>','aar.dev@trigyn.com','aar.dev@trigyn.com',now(), 2);