SET FOREIGN_KEY_CHECKS=0;

replace into jq_manual_type (manual_id, name, is_system_manual,header_template) VALUES ('07cf45ae-2987-11eb-a9be-e454e805e22f', 'JQuiver Manual', 2,'<script>
	$(function() {
		//console.log( "ready!" );
	});
</script>
<style>
    /************** Header block********************/
    .helptopblock{

    }

    /************** Hamburger and menu block********************/
    .lefttopcontenctcls {

    }
    
    /************** Tree menu left  block********************/
    .Helpmanualblock .leftcontenctcls{

    }

    /************** Spiral Image********************/
    .spiralbinding{

    }

    /************** Tree menu block********************/
    .jstree-container-ul


    /************** Tree menu node********************/
    .jstree-default>.jstree-container-ul>.jstree-node

    /************** Tree menu icon********************/
    .jstree-default .jstree-file{

    }
    

    /************** Main content  block********************/
    .Helpmanualblock .helpmanual_right{

    }

    /************** Expand collapse button ********************/
    .resapiactionbtn{

    }

    /************** Top right button ********************/
    .righttopbuttons > span{

    }

    /************** Top Search Block ********************/

    .searchhelp{
        
    }
</style>');

/*************************************************JQuiver - start****************************************************************/
REPLACE INTO jq_manual_entry (manual_entry_id, manual_id, entry_name, entry_content, sort_index, last_updated_ts, last_updated_by) VALUES
('57bdec61-325a-4487-9ba6-de218207f8c0', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'JQuiver', '# Purpose 
Objective of this project to rapdily develop Enterprise Web Application. Ideally in normal situation anyone with basic knowledge of HTML, JavaScript, JQuery, CSS & SQL (MariaDB / MySQL / PostgreSQL) will be able to work on the project. So people from different development segment will be able to work together.
Also the development is live development. Developers can work right on the SIT system. While the manager can monitor the development live.
Trigyn has been working with number of Web Application project, which consists of components like Grids, Forms, Templating. In-order to speed up the initial project setup and components configuration there was a need to develop a reusable component. 
# Modules
Maven Group-id :- com.trigyn
1. **Master Generator** : -Master Generator plays a pivotal role while creating an application by using JQuiver framework. Helps to create any master modules without any much effort. It helps to design the Listing page, Form Builder and Router from the table available in the corresponding DB, to support the application. 
2. **Grid Utils** : - (gridutils) While developing any web application, there is always a need to show listing page, wherein the user can sort the data, filter it and keeping the performance of application in mind the data should be loaded lazily. 
3. **TypeAhead/Autocomplete** : - Built using frontend library, rich autocomplete to get data lazily or dependent dropdown, easy custom UI.Developer just need to write SQL query and frontend component will start working.
4. **File Bins** : - Create File Bins for the application for uploading files. Users can include file bin in any form, template or module and upload multiple files of any format, size as per their requirements. There is a provision for Custom File Storage also.
5. **Templating** : - (templating) Many web applications contain certain structure or pattern to be followed while display views, sending out mails etc. A template is a form or pattern used as a guide to make something. They can be of various types like form templates, listing templates, email templates and rest-client templates. Generates HTML web pages, e-mails, configuration files, etc. from template files and the data your application provides. 
6. **Form Builder** : - (dynamicform) Suppose there is a need to build a simple form, where you have to perform CRUD operations on one or two entities (usually for master tables), you can use this module provided by JQuiver.The workstation where you can create, edit, and manage a form can be called as a form builder. Forms collect different types of data through the fields present in them. These fields can be managed and customized from its form builder. Helps to create dynamic forms for the web application, without writing any java code just by using free marker, java script, python, php, RoR, Java etc. At one place developer can write the display and persistence business logic. 
7. **REST API** : - (dynamic-rest) Representational State Transfer (REST) is an architectural style that defines a set of constraints to be used for creating web services. REST API is a way of accessing web services in a simple and flexible way without having any processing. REST APIs are efficient, high-performing, consume less bandwidth, and are cost-effective because developers can use them without third-party tools. One REST API can be published in 5 minutes.
8. **Router** : - Routing is the module that maps the UI tree nodes to the URL paths and vice versa. Each branch of the tree is assigned a path. The route (URL path) to a node is obtained by joining all paths from root to the node by /. This assignment drives UI navigation in the sense that the UI view for a given URL path consists of the UI view snapshot mentioned above by composing all web components on the path. i.e., for that URL path, only the web components on the path are in the DOM and contribute to the UI rendering. It helps in creating menu for any application effectively, efficiently and effortlessly. This also have a DnD UI to adjust menu.
9. **Internationalization** : - (resourcebundle) The process of designing web applications in such a way that which provides support for various countries, various languages, and various currencies automatically without performing any change in the application is called Internationalization(I18N). It is known as I18N because between I and N; there are 18 characters; that’s why I18N. It helps to display messages, currencies, dates, time, etc., according to the specific region or language. 
Features for Importing and Exporting Resource Keys is given in the module. The keys are exported and imported in files in the excel format. 
10.  **Dashboard** : -A dashboard is a page that consists of multiple components, mostly independent of each other. Dashboards are reusable, customizable, can display different types of information at one place. It is an information management tool that receives data from a linked database to provide data visualizations. Dashboard use data visualization to help businesses make real-time decisions. They help to create the daily reporting, application usage, trends dashboard for web application and controls it with the dashboard admin panel. Dashboards can help ensure that everyone has access to the latest data and insights, which can lead to better decision-making. 
Dashboards can be customized, which allows different people within an organization to use the same data in different ways.  
Dashboards can display information in real-time, which can help decision makers act quickly.
	11. **Dashlet** : -A dashlet is an individual component that can be added to or removed from a dashboard. They are a reusable unit of functionality. When the dashboard loads it should just show the UI of the dashlets . x
After the page load each dashlet sends a server call (based on its type) to fetch its data. After the data has been fetched, each dashlet (based on its type) renders the data. Dashlets render the information in UI that needs to be displayed through the dashboards . Information can be of various types like Server Listing, CPU Usage etc. as per the user’s requirement. 
Dashboards are containers consisting of multiple dashlets. They control the way the dashlets are organized in dashboards.
12. **Notification** : - (notification) A notification module is a software component that manages, creates, and sends notifications to users. When an event occurs, the module generates a message based on predefined rules or templates. It helps to create application notification with ease and control the duration and context where to show it, (cross platform).
13. **User Management** : - User Management is a system for authenticating users and storing user data. Our User Management APIs make managing users of any application easy. These APIs handle user registration process, user authentication, and password management. We provide a variety of login options including Database Authentication, LDAP, OAuth and SAML integration. Using our User Management module, you can manage all aspects of your user accounts such as changing user properties, reset passwords, enable or disable users, manage user profiles, and more.
14. **Application Configuration** : -JQuiver provides with a database level implementation of application configurations, wherein user can manage all environment and system related properties.JQuiver has in-built classes and methods to configure mails. 
To enable mail configuration , "Mail Configuration" page is provided in the Application Configuration Module. Here, the details like, SMTP Host, SMTP Port, Security Protocol, as per the user mail server is provided. 
15. **Help Manual** : - A help manual, also known as a user manual or instruction manual, is a document that provides instructions and information for using a product. It can help users learn how to install, troubleshoot, and operate a product.It provides detailed information about a module which can be helpful for users to troubleshoot their issues on their own, which reduces the workload on customer support and also save time. 
16. **Form IO** : - Form.ios Developer Portal provides you with the drag-and-drop Form Builder that automatically generates the necessary API.
17. **Import and Export** : - This module provides provision for importing and exporting different modules and configurations based on the user’s requirement.The file is exported and imported in zip format. The permissions along with the modules are also imported. Many user friendly options are given.
18. **Script Library** :- A script library allows you to store classes, functions, and variables for common use within an application. A script library is either a Client Library or a Server Library. The scope of a script library is the current application.It can be included in the REST API Module, Form Builder Module and File Bins Module. It is optional based on the user’s requirement it should be included. 
Unlike source files for other programming languages that must be compiled into bytecode before you run them, scripts are evaluated by a runtime environment (in this case, by a script engine) directly. 
Provisions for JavaScript, Python and PHP Script Engines are provided in the application. 
19. **Custom File Storage** : - A custom file storage module is a programmatic system or library built to handle the creation, storage, retrieval, modification, and deletion of files. Instead of relying on a pre-built library or system, this module is designed with custom logic tailored to meet specific requirements of an application or project. A custom file storage module is an excellent choice for applications with unique requirements, offering control and efficiency that pre-built solutions might not provide. 
The module typically abstracts file operations, allowing developers to interact with files in a structured and secure manner. You can tailor the module to fit specific business needs, such as custom directory structures, unique naming conventions, or specific data formats. Allows seamless integration with other components in your system. 
You can implement custom encryption, user authentication, and access controls, ensuring secure file handling. Reduces reliance on third-party libraries that may have security vulnerabilities. 
20. **API Client** : - An API client is a set of tools and protocols that operate from an application on a computer. They help you to bypass some operations when developing a web application rather than reinventing the wheel every time. Using a client API is a great way to speed up the development process. The API Client authenticates the client making a request. It also provides authorization letting the API know if the requesting application is allowed to use the API and what it can access. 
21. **Scheduler** : -Scheduler is used to schedule a thread or task to execute at a certain period of time or periodically at a fixed interval. There are multiple ways of scheduling a task in Java. Timer Task is executed by one execution demon thread. Any delay in a task can delay the other task in schedule. 
Our Application also has other scheduling frameworks like Quartz Scheduler which offer more advanced features and capabilities for scheduling jobs in a distributed environment. These frameworks support clustering, load balancing, and failover, making them suitable for enterprise applications. Scheduler API allows users to schedule tasks for future execution, or to run at specific intervals or at fixed times. Scheduler can be used to run background tasks, process user requests, or perform other system-related functions. 
21. **Visual DnD Organiser** : -JQuiver supports drag and drop (DnD) for transferring information between components in an application. This is incorporated for handling the different Router Menus in the application.User can directly manipulate the menus created from the UI with the features of DnD Organiser. The sequence which is maintained on the UI will be maintained in the DB.
22. ** View REST API** : -This module helps to view all the custom REST APIs created in the application. Users gets an easy view of the different Data Parameters and the Response Header.Export Option is also given for exporting the selected REST APIs in PDF format. 
Sorting Option is also provided for the user to export in their desired sorting order. 
', 0, NOW(), 'admin@jquiver.io');
/****************************************************JQuiver - End****************************************************************/

/*********************************************Application Configurations - Start***************************************************/

REPLACE INTO jq_manual_entry (manual_entry_id, manual_id, entry_name, entry_content, sort_index, last_updated_ts, last_updated_by)
VALUES('918676c8-b653-43ee-964a-d4faaeb13787', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'Application Configurations', '# Property Master Details

Steps to create Application Configuration.

**Step 1: **Visit the Application Configuration module on Jquiver home.
 ![](/cf/files/6dab2b82-c56b-4b75-8363-744746ec7ec0)
 
**Step 2 : **Click on Add Property, it will open up a form to add/edit Property.
![](/cf/files/f572d5d4-61ab-4dbd-9b87-1b73b24041c2)

**Step 3 : ** Fill out the required fields as given below and save the details.
![](/cf/files/57b0a742-8e3a-48a1-b8d3-487c91725ac5)

**Step 4 : **After saving, the property will appear in the listing page as shown.
![](/cf/files/f4dee366-0621-41f2-aed2-33f81ab280c2) 

**Step 5 : **We can edit the Property in the Edit mode.
![](/cf/files/b5fab228-0db4-407a-9f50-c28a45c703af)

Jquiver provides with a database level implementation of application configurations, wherein you can manage all you environment and system related properties.
Property Master table contains below configurations :

| Property Name | Usage |
| -------- | -------- | 
| authentication-type     | Authentication type - default in memory authentication     |
| enable-user-management     |By default user management will be disabled     |
| max-version-count      | Maximum limit to persist versioning data |
| regexPattern     | Regex Pattern to validate password     |
| user-profile-form-details     | Dynamic Form for add edit user details     |
| user-profile-template-details   | Custom template for user profile listing page     |
| user-profile-form-name     | Dynamic Form for user profile     |
| enable-google-analytics     | To add google analytics on each screen     |
| google-analytics-key | Google analytics key,that needs to be passed to GA     |
| jws-date-format     | Date Format to be used at various levels such as on, UI layer, Java Layer, and Database.Stored in JSON format     |
| version     | JQuiver Application Version    |
| profile     | Checks the profile in which the application is running.      |
| template-storage-path     | Path at which the template will be stored during local development.    |
| base-url     | To fetch the base url of your application.    |
| maxFailedCount     | After 5 attempts of unsuccessful login, the users account will be locked.    |
| jwsSecretKey    | Default secret key to be used in appropriate requirements.    |
| JwkProvider    | An interface that provides access to JSON Web Keys (JWKs)    |
| last-deployed-version   | Depicts the last deployed version.    |
| passwordExpiry   | Sets the password expiry limit.    |
| isActivityLogEnabled   | By default ActivityLog will be disabled.    |
| last-deployed-date   | Date on which the last version was deployed.    
| adminEmailId   | Email Id with which you sign into the application.    |
| file-copy-path   | Path where the files would be stored.    |
| otp_expiry_time   | Time limit set for the OTP to expire.    |
| default-redirect-url   | Default redirect URL set.    |
| auto-delete-notification   | By default the notifications are automatically deleted.    |
| mail-configuration   | Stores the configuration of the mail server .    |
| clusterActiveTime   | Sets the active cluster time .    |
| scheduler-url   | Sets the default scheduler url.    |
| daily-notification-count   | Sets the daily notification count.    |
| notification-validity   | Sets the validity of the notification.    |
| file-upload-location   | Sets the file upload location.    |
| isStaticImportDone   | Check whether database is imported for the first time.    |
| acl-jws   | Default role for jws.    |



You can run JQuiver Application in 2 modes, 1. Development Mode. (dev) 2. Production Mode. (prod)

By default the production mode is enabled. In this mode the templates, and forms are editable only through the UI-Editor provided by the JQuiver application. 
Whereas dev mode enabled the user to download the Template, Form Builder templates to your local machine and enables you edit it locally. This modes can be edited using **profile** property in Application Configurations module.


 ![](/cf/files/b5fab228-0db4-407a-9f50-c28a45c703af) 


**Mail Configuration**

JQuiver has in-built classes and methods to configure mails.
To enable mail configuration goto "Mail Configuration" page, by clicking on the icon.

![](/cf/files/36675e75-029e-4d46-966f-f5060ae3099a)

Here provide the details like, SMTP Host, SMTP Port, Security Protocol, as per your mail server by default the following values are set as shown in the image.

![](/cf/files/22f806b9-3ab1-45db-90de-575651d64e0a)
![](/cf/files/4abc8a9f-bf90-4008-a517-30c06ec6e4e6)

This page also allows you to set the "From" and "Reply To" mail ids and also to attach files as shown in the picture above.

By clicking on the save configuration, the email configuration property gets saved in the property_master table.
![](/cf/files/37bb5551-34fb-417f-a7f4-c59a6645ae00)
![](/cf/files/5a852acb-d095-44e5-af60-62f558a8c1d3)

There is a provision to send test mail by clicking on the button.
![](/cf/files/a410b60d-4687-4c18-b7fa-9e744acfffaa)

There is also provision for attaching footer and files to mail as shown below.
![](/cf/files/740c4dd8-4e35-4691-bc53-da82942b1e0b)

For receiving the mail you can configure Fake SMTP in the mail-configuration property in property_master table.After configuring the mail configuration, mail is recieved as shown below.
![](/cf/files/f072bd74-00fd-4a56-b928-c71f622a0287)

Application configuration also provides you with enabling Google Analytics, Create a Google Analytics Key and add it in **google-analytics-key** and also you need to set the property of **enable-google-analytics** to **true**
 
 ![](/cf/files/fc650462-72af-4a16-a074-2829d6d1c84f)' , 2, NOW(), 'admin@jquiver.io');
 
/*********************************************Application Configurations - End***************************************************/
 
 
 
/****************************************************User Management - Start*****************************************************/

REPLACE INTO jq_manual_entry (manual_entry_id, manual_id, entry_name, entry_content, sort_index, last_updated_ts, last_updated_by)
VALUES('e03447c8-eaa0-4119-b97e-b802bd8f4ff1', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'User Management', '# User Management 
User Management is a system for authenticating users and storing user data. Our User Management APIs make managing users of any application easy. These APIs handle user registration process, user authentication, and password management. We provide a variety of login options including Database Authentication, LDAP, OAuth and SAML integration. Using our User Management module, you can manage all aspects of your user accounts such as changing user properties, reset passwords, enable or disable users, manage user profiles, and more.

Steps to enable User Management.

**Step 1: **Navigate to User Management from home page.
![](/cf/files/1e6fc271-65d7-4f92-840e-4c070192081a)

**Step 2: **Enable the Authentication toggle button.
![](/cf/files/03abf3a9-f621-4b52-89c2-c0532b7e378f)

**Step 3: **Choose the Authentication Type from the dropdown.
![](/cf/files/60d7a5b6-2338-4142-95e0-b3bcd79c7944)

**Step 4: **Then save the configuration.
![](/cf/files/8b2d5d39-8610-4e5d-b267-23248793530a)

**Step 5: **You can add multiple authentication based on your requirement and save the configuration.
![](/cf/files/2daa37b8-f09d-4ab3-ba44-8efbd2528a72)
![](/cf/files/662d815b-07e4-4507-a17e-3d46f8bb09c7)
![](/cf/files/d68df2d8-2b39-4ffd-b25c-ab1d6310ce53)

These module provides the following features:  
* Manage roles -
* Manage users - 
* Manage permissions - 

![](/cf/files/ed8eeba0-7381-4d01-bd8d-6d13165e6f02)

** Manage roles: **By clicking on Manage Roles icon, you can navigate to the Role Master Listing page. 
![](/cf/files/3323e9a9-075e-41f5-a1b6-0bce405da169)
 
There are 3 predefined system roles  : ADMIN, AUTHENTICATED, ANONYMOUS  which you cant edit. 

**Step 1: ** By clicking on the Add Role button, you can add new Role in the Add Role Page and save it.
![](/cf/files/40232f8b-c302-4d82-b155-c92b21600022)

You can add/edit  role  and set as active or inactive depending on your use case.
  
** Manage users : **By clicking on Manage users icon, you can navigate to the User Master Listing page.
	 There is 1 predefined user which cant be modified : admin user
	While  add/edit user by default AUTHENTICATED role will be assigned and disabled.
	
** Step 1: **By clicking on the Add User button, you can add new User in the Add User Page and save it.
![](/cf/files/501b9200-a556-436c-8474-33c14b3c1593)
![](/cf/files/f2c32ac3-eb0f-46c9-9007-392da106c74f)
	
	You need to enter basic user fields "name", "email"  etc..   and assign role.
	You can active/deactivate a user through "is active" field.
	You can configure whether to send mail or not while a user is created/updated, using Send Mail option.
	 If you enable "force user to change password" field  then a mail would be sent to the user
	 mail id  for changing the password, mandatorily. You wont be able to off the Send Mail property, in this case.
	The template used for email is : "force-password-mail-subject" and "force-password-mail"
	You can modify if you need to alter it.
	
**Manage Permissions: **By clicking on Manage Permissions icon, you can navigate to the Manage Permissions page.
![](/cf/files/a56d471a-2aa0-4355-a166-aff429db4d3c)
	 
	 It contains all the entites which are made up from various modules  present in the application 
	 if we want to remove a particular entity access just uncheck it from the grid for that role. 
	 Only those modules will be visible whose module_type_id = 0 is in the table jq_entity_role_association

There are 4 types of authentication:

1) Database
2) LDAP
3) OAuth
4) SAML

![](/cf/files/60d7a5b6-2338-4142-95e0-b3bcd79c7944)


**Database Authentication **  There are 3 different ways to configure database authentication :
1) OTP
2) Password
3) TOTP

![](/cf/files/d512723b-5234-48c6-8981-68986e3dbbd1)
 
 There is a provision for enabling captcha validation during authentication.
 
 ![](/cf/files/2e424b52-74fd-478a-a9de-54fa5ef899f1)

** Password + Captcha **-  Email id + password  + captcha login 

While logging into the application after enabling authentication, the email id is given admin@jquiver.io and default password is given as Account@123 as shown below.
 
![](/cf/files/d3dcb795-33a3-4dba-a457-be1afc3cde8f)

After clicking on Sign in button, it gets redirected to Change your password page where we need to change the default password to your desired password. 

![](/cf/files/9c7b44b9-22b6-416b-9781-b0f97b48a45e)

After changing the password , you can log into the application.

**OTP **- Email Id + OTP login
![](/cf/files/d1964d46-c60d-4f92-9d27-db0d234fe243)
![](/cf/files/90b3dd04-3fcd-4172-be7e-265658f8256f)

For sending OTP to your registered mail id, you need to configure your mail configuration whether SMTP or any mail server.
![](/cf/files/062399fe-986c-4ce4-b1c2-17a57c2a0953)

After that by entering the OTP received in mail, you can log into the application.
	
**	TOTP **- Email Id + TOTP login
	
![](/cf/files/f072cbdd-5ec6-46cc-b25a-567a3bee20e3)
![](/cf/files/bdbb2a8b-308e-4dfc-8914-195511acac19)

After this you have to configure TOTP by clicking on the link Not Configured? Click here as shown in the above image.
After this it gets redirected to Reset your password page where you have to enter your email id to receive the TOTP configuration.
![](/cf/files/72b2d24b-9adf-4ddc-ac3b-215ef2921c28)
![](/cf/files/2b2e833c-865e-484d-bbe8-5dc977a961cc)

**OAuth** : There are 3 clients for signing through OAuth . They are as follows
1) Google
2) Facebook
3) Office365
For OAuth, Force Password Change option will not be available.

![](/cf/files/2daa37b8-f09d-4ab3-ba44-8efbd2528a72)


**Google OAuth Steps** - 

1) Go to https://console.developers.google.com/apis/credentials  

2)  Refer above image  click on project
 
![](/cf/files/4028b881765fd65701766028c6f60001)
 
3) Add project name and  and keep  location as default value, then click on create
 
![](/cf/files/4028b881765fd6570176602a9c080002)
 
4) Click on "+ New Credentials" > O Auth client Id > Click on configure consent screen 
   1) Select on  external user type  > create button  >
	 
![](/cf/files/4028b881765fd6570176602ca2d50003)
		
   2) Add mandatory fields in step 1 and keep rest of the steps as default and continue
   
![](/cf/files/4028b881765fd6570176602d10ac0004)
		
 5)Click on Credentials tab  >  Click on + new credentials > O Auth client Id > select web application  and add application name
    1) Scroll down and add google redirect uri http://localhost:8080/login/oauth2/code/google
    2)Clck on create after that a pop up would be displayed with client id and secret id > copy it 

![](/cf/files/4028b881765fd6570176609447520005)
	 
![](/cf/files/dd858d3a-dc56-407c-9359-99a7c43c9f3a)
![](/cf/files/91f5ad0e-5ea5-4df1-a50a-2571540d7e3d)


**Facebook Oauth Steps** -
1) Go to https://developers.facebook.com
2) Click on myapps    > Click on create app >Choose Build Connected Experiences and continue  >Enter App display name  and rest as default and click on create app 

![](/cf/files/4028b881765fd657017660d3c0550006)
 
3) Click on  Settings tab > Click on basic  you will get client  id and secret id

![](/cf/files/4028b881765fd657017660d441bd0007)

![](/cf/files/57a259c3-7458-4f5d-88e6-a9157409abcc)
![](/cf/files/9ad4cb86-db50-4bd9-b5d7-592cf9778714)


**Office365 Oauth Steps** -
1) Click on https://portal.azure.com/
2) Click on Azure active Directory> Click on app registration > Click on new registration

![](/cf/files/4028b881765fd657017660d70c6d0008)

3) Enter name > choose single tenant  > choose web >scroll down to bottom and add redirect uri Example http://localhost:8080/login/oauth2/code/office365 > Click on register 

![](/cf/files/4028b881765fd657017660d8b7380009)

4) After that you will be reidrected to overiew > Copy the Application (client) ID 

![](/cf/files/4028b881765fd657017660d92df9000a)

5) Click on Certificates And Secrets tab  > Click on New Client Secret  >  Enter description for client and click add

![](/cf/files/4028b881765fd657017660da0caa000b)

6) Copy client secret value from "Value" column data  and maintain safely  it is secret id 

![](/cf/files/4028b881765fd657017660da9464000c)

![](/cf/files/eafca097-b1f7-46c8-8ba9-9b8b9d4ea806)
![](/cf/files/405cf635-0629-4cd2-a555-f37c0f969a10)


**Note:** After restarting the application the login screen will look like this:

![](/cf/files/855c65ef-0749-4fdd-ad3e-8ece7381e80d) 

**SAML** : Steps for enabling SAML Authentication.

** Step 1: ** Use HTTPS instead of HTTP in the URL.

** Step 2: ** Following are the steps to configure HTTPS in your springboot application:
1.	Create a Self-Signed Certificate.
keytool -genkey -alias tomcat -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 3650
The above command should be in single line or else some error will be thrown. 

![](/cf/files/2b00768c-6d3b-4e21-9c38-e6764de990a4)

This code will generate a PKCS12 keystore file named as keystore.p12 and the certificate alias name is springBoot.

2.	Configure HTTPS
We need to provide key-store file path, key-store-password, key-store-type and key alias name into the application.properties/yml file. Any port number can be used.

![](/cf/files/d5d4098a-3ac8-4bfd-a53a-bddeca3be413)

Note:  Store the keystore.p12 in the jws\maven-common-utils\java-web-starter\src\main\resources folder i.e referred to as classpath.

Then restart the application, use the URL https://localhost:8060/demo
You can choose your convenient context-path.

3.	For adding SAML in JQUIVER application, following dependencies have to be added in pom.xml of java-web-starter.

![](/cf/files/71b5a6ba-0ba2-490f-844a-e609463992ab)

4.	Identity Provider Configuration (Okta)
The Identity Provider used is Okta, so after creating a free Developer account, let’s create our Application. Follow the steps:

![](/cf/files/68812ef5-9384-4f6c-be0f-b814d12cd5fb)

Click Add Applications in the top right of the Dashboard to continue. This will bring you to a screen with a Create New App green button on the left of the screen.

![](/cf/files/bbee2a7a-99ef-42c9-9f23-53bad117fab8)

Choose SAML 2.0 for the sign on method. 
The next screen will take you to the General configuration page.

![](/cf/files/d2d8ee5b-ccf6-4540-aceb-257461346cba)

For configuring the SAML screen. Enter the following values:

![](/cf/files/2ddbb6f7-a479-4aa3-9035-16d101567631)

Then  Choose “I’m an Okta customer adding an internal app” and optionally select the checkbox of the App type.

![](/cf/files/60afcd54-19b2-44d8-880b-c2407b168c40)

Copy the Identity Provider metadata link from the yellow box as you’ll need it during the configuration of your Spring Boot application.

![](/cf/files/7c5f5b84-d4bb-4e6a-99e0-571701486dc8)

Click on the Assignments screen/tab and the Assign > Assign to People button to add the users. 

![](/cf/files/467f02a8-e2b5-49a6-a441-b99280887972)

After all this you need to set the First Name, Last Name and Email in the Attributes section.
These information is needed in the SAML Assertion Response for Authentication.
This is available in the second tab i.e Configure SAML.

![](/cf/files/795650d6-1c12-4b45-a03d-75f897198e31)
![](/cf/files/b05fa373-b29a-43a1-ae2f-3601ba5966d7)

So, after following the above mentioned steps, we are set to use Okta as IdP in our application.
After this, enable Authentication by going to User Management Module and enable SAML authentication. After saving and restarting , you need to provide the following credentials in the UI Screen.
For Logging Out of Okta and redirecting to home page, you need to provide two inputs.
1.	SAML Log Out URL : https://app-domain.okta.com/login/signout
E.g.: https://dev-21383837.okta.com/login/signout
2.  SAML Base URL: Your application base URL
E.g.: https://localhost:8060/demo

![](/cf/files/d6c6ae56-b832-41ff-94a3-6b3b08bf890c)
![](/cf/files/a1a365ef-cb6c-41bb-835e-31ea2b9f74c4)

Here the credentials are as follows:

Registration Id -----------okta
Assertion Consumer Service Location ---https://localhost:8060/demo/login/saml2/sso/okta
(This you will find in the Okta’s General Tab Single Sign On URL or the Recipient URL both are same.

Meta Data URL ----https://dev-21383837.okta.com/app/exkmhwesbmJOSQ7h45d7/sso/saml/metadata 
(This you will find in the Okta’s Sign On tab ---- Metadata URL

IdP Web SSO URL -------https://dev-21383837.okta.com/app/dev-21383837_jquiver_1/exkmhwesbmJOSQ7h45d7/sso/saml
(This you will find in Okta’s Sign On Tab ---- View SAML setup instructions ---- Identity Provider Single Sign-On URL.

After this sign on with the URL of your application, it will automatically redirect to Okta and after authenticating, you will be able to sign in through SAML in your application.

The login screen after enabling SAML Authentication will look like as shown in the picture below:

![](/cf/files/3a2ff958-a2da-4acb-9523-180591f3d69b)

**LDAP** : Steps for enabling LDAP Authentication.

** Step 1: ** You need to have access to VPN.

** Step 2: ** You need to provide the following configurations.

![](/cf/files/9901102a-2c43-49ca-bd91-6143f166c2a4)
![](/cf/files/858fce27-019e-4844-8225-1ec9cb3ca94f)

After enabling LDAP Authentication, the login screen will look like the picture below:
![](/cf/files/f6a6d95f-c5c4-4bf2-b21e-7e7edac001ce)
 
', 3, NOW(), 'admin@jquiver.io');

/***************************************************User Management - End********************************************************/



/****************************************************Master Generator - Start***********************************************************/

replace into jq_manual_entry (manual_entry_id, manual_id, entry_name, entry_content, sort_index, last_updated_ts, last_updated_by) VALUES
('61fb4be9-a197-4759-9a4c-b885ce973f46', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'Master Generator', '# Master Generator

Master Generator plays a pivotal role while creating an application by using JQuiver framework.
It helps you to design the Listing page, Form Builder and Router from the table you have, to support your application.

**How to make use of Master Generator**

First step to create any application will be designing your database schema and creating the table. Same will be needed in JQuiver application. To make use of Master Generator, you custom tables should be in place, firstly.
Once it is done, go to Master Generator module.

**Step 1: **Navigate to Master Generator module from home page.
![](/cf/files/83cb09ff-9977-44ce-b452-a2dba3b49660)

** Step 2: **Select the table from the Select Table list. This list will contain only the custom tables which are created by the user.
 ![](/cf/files/37329146-291f-4715-ba85-4e8817da203b)
 
 ** Step 3: ** Once the table is selected, then provide the module name as per your desire.
 
 ** Step 4: ** Next comes the designing of listing page. In this section, you can decide which all columns from your table should be visible on the listing page. Also it helps you to configure, if at all any column should be kept hidden and be not visible.
 ![](/cf/files/83eb3548-6860-4f0f-bbb6-c40520a84436)
 
 ** Step 5: ** When we select the custom table, then automatically the corresponding router module for listing-page is created and the router menu is enabled for the listing page.
 ![](/cf/files/6c2c11bd-1327-46fb-aac9-5860d1660e76)
	
 ** Step 6: ** Now comes  the Form designing. This section will help you to design the add/edit page, for the corresponding table you have selected.  Now as in listing, here also you can select the fields which should be visible, which should be hidden and which should not be considered, at all, which doing the add/update of data.For configuring Form, the corresponding router module for Add-edit page is created and the router menu is enabled for the  Add-edit page.
 ![](/cf/files/ef2f1bd0-765e-468a-80b2-d72fc2dbc436)
		 
 ** Step 7: ** For enabling Captcha and CSRF validation , the corresponding Toggle Check Boxes are provided, which should be enabled or disabled as per the user requirement.
 ** Note:- ** CSRF validation can be done only if Captcha validation is enabled.	
 ![](/cf/files/9b4ee2f0-b396-49bc-ba4b-05cda7dde900)

** Step 8: ** For including File Bin in the form , the corresponding File Bin Toggle Check Box is given, upon enabling it, the file fin will be included in the form.
	![](/cf/files/3b42f9b0-1cc5-4e43-8381-9dcf29e8bf64)
	
 ** Step 9: ** If the user wants to include File Storage Class in File Bin, then he/she can enable the File Storage Toggle Check Box. Details about File Storage Module is given in the File Bin Module.
 ![](/cf/files/bfe0ff63-c318-4f3c-a7dd-8f6196bb309f)
	
 ** Step 10: ** Now comes  the main part. Permissions. As in all module, you can provide permission to this pages as well. Using this, you can restrict the usage of the page by the users as per their roles.	 
  ![](/cf/files/61660baf-f11b-40fb-92a1-523ea72153f2)
		
 **Note:-** Before saving, do a cross verification on the entries and options you have made, while creating the master generator, as we have not provided edit option for this module. Once created, you will not be able to edit it.
		
**	Output**
 Once this module is created, the menu will be available in the application as shown below.
 ![](/cf/files/63e3a766-0041-4a23-818e-1ccc4bfd2af3)
	
Clicking on the menu will redirect to the employee listing page.	
![](/cf/files/81fd4610-eb9a-43f8-b372-bc5611a9aeb2)
	
As in the above image, the listing page will have two options. One is Create New and another is edit. This are the feature, which avoid us to create separate pages for create and update.
	
Opting for Create New, will enable you to create a new employee. This page will have only the fields which you have marked as visible/ hidden.
![](/cf/files/cda41ffd-11d1-469f-946e-d05125124084)
	 
Opting for Edit, will enable yo to edit already existing entries.
![](/cf/files/09b4871b-570f-445d-aca7-cbcd2c2554fa)

Logically, if we create a Master Generator, entries in 4 modules are done.
1. Template
2. Dynamic Form
3. Router
4. Grid

Taking an example of above scenario, 
* Listing page for employee will be created in Templates. You can update the template to customize the page as per your requirement.
![](/cf/files/2b375711-52d1-4227-8bab-0a71f2ad0a06)
![](/cf/files/8c0c1071-6304-4937-8b04-25a9ab708809)

* Add/Edit form is created in Dynamic Form. To customize this page, dynamic form has to be modified.
 ![](/cf/files/546a81e2-8f14-468c-8f9c-69217cdc8567)
  ![](/cf/files/e2795bd4-c8a2-4c24-b884-77907843225f)
	
* To add a menu entry, entry in Router is done through Master Generator.  
 ![](/cf/files/d3f2a0d3-f362-4f1f-9f68-8c01c9e0ea26)
 ![](/cf/files/64661577-d84b-423e-bc73-73aeebb6cf3a)
 ![](/cf/files/3939ab4c-b91f-448e-ab5e-b02a176f039c)
 
 * Grid page for employee will be created in Grid Utils.
  ![](/cf/files/2e898ae2-bcf3-4a42-985f-587d682f7e81)
  ![](/cf/files/a0246bf6-0b79-4729-9032-b06f0f4fd6ab)
		 
', 4, NOW(), 'admin@jquiver.io');

/****************************************************Master Generator - End******************************************************/



/****************************************************Router - Start*********************************************************/

REPLACE INTO jq_manual_entry(manual_entry_id, manual_id, entry_name, entry_content, sort_index, last_updated_ts, last_updated_by) VALUES
('68613ad7-8596-4c48-94e6-f752ab53eb4e', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'Router', '# Router

You just need to add DBUtils as a dependency in your application to enable Router.

Steps to create insert new entry in menu.

**Step 1: **Navigate to Router module from home page.
![](/cf/files/6d1317e0-2c6d-42cc-a114-87e35a325214)
 
**Step 2: **Click on Add Route button.
![](/cf/files/5e749fe5-4090-42f1-aafa-72e017d3d170)

**Step 3: **Provide all mandatory details and click on save button.
![](/cf/files/8a665c76-6ea7-4f82-a4e4-1b5401a601a5)
![](/cf/files/a56833f3-0ee3-42ff-8311-aef0375f89a1)

**Note: **
While adding new Route in menu make sure there is no other record exist with same Route name or with same module URL. 
If Include in Menu enabled then sequence number is unique in selected parent module.

You can create Route for any one of the following context type:
* Dashboard
* Form Builder
* REST API
* Root
* Target URL
* Template

Except for the Root as a context type, context name autocomplete will be enabled. From context name you can select any entity created from the respective context type master.

Example to include dashboard in the Router:

**Step 1:** Create new Dashboard from the dashboard master (You can refer Dashboard help manual for more information on how to create/manage dashboard)
 ![](/cf/files/c416c1d5-465c-49e2-96b9-7cc10459f4b2) 
 
**Step 2:** Enter Route name and enable Include In Menu toggle button.
![](/cf/files/0b75821b-3fc6-4c90-a439-5b58cae08edf) 

**Step 3:** Select Dashboard as a Context Type. When Dashboard is selected as Context Type then automatically Include Layout toggle button is enabled.
![](/cf/files/40f73b6e-c35f-45bc-86a5-683f8b60b668)

**Step 4:** Enter dashboard name that you have created in step 1 in Context Name.
![](/cf/files/8e1d8100-d202-487f-a925-b18a7b3c803d)
 
**Step 5:** Enter Route URL.
![](/cf/files/d5c00cf7-9caa-4102-a33c-57bdbff31901)


Example to include Dynamic Form in the Router:


**Step 1:** Create new Dynamic Form using Form Builder (You can refer Form Builder help manual for more information on how to create/manage dynamic form)
![](/cf/files/ff125cd6-db4e-483c-8ada-be6b75c690d5) 
 
**Step 2:** Enter Route name and enable Include In Menu toggle button.
![](/cf/files/d1085c0f-a2c0-42ca-a760-3ebfaa528059) 

**Step 3:** Select Form Builder as a Context Type.
![](/cf/files/3c9b48c8-e289-4dc0-adcb-d65d15ca585a)

**Step 4:** Enter form name that you have created in step 1 in Context Name.
![](/cf/files/78162486-74c6-4312-a34b-f96e099e8896)
 
**Step 5: ** Enter Route URL.
![](/cf/files/ee3b19bf-fa9f-446a-baac-0cd88c7da3e3)


Example to include REST API in Router:


**Step 1:** Configure new record in REST API master(You can refer REST API help manual for more information on how to create/manage REST API)
![](/cf/files/d9dc6447-f608-4b8a-9350-d24dd4bd483e)
 
**Step 2:** Enter Route name and enable Include In Menu toggle button.
![](/cf/files/815a77bf-67cc-4232-81ba-3a162508e55d)

**Step 3:** Select REST API as a context type. When REST API is selected as context type then automatically Include Layout toggle button is enabled.
![](/cf/files/23db4a30-f453-469d-b4a5-ddf1947906e6) 

**Step 4:** Enter method name that you have created in step 1 in Context Name.
![](/cf/files/1151237b-2e22-4211-a906-bf541daa90fa)
 
**Step 5: ** Enter Route URL.
![](/cf/files/41b6419b-a5db-4cb3-85b9-8ad9bbac1d72)



Example to include Template in the Router:


**Step 1:** Create new template from Templating module(You can refer Templating help manual for more information on how to create/manage Template)
![](/cf/files/bde187ec-841d-4bc6-a6d4-9b190522719b)
 
**Step 2:** Enter Route name and enable Include In Menu toggle button.
![](/cf/files/ef034de4-f415-46e6-be68-e7a70525f51f) 

**Step 3:** Select Template as a Context Type. When Template is selected as Context Type then automatically Include Layout toggle button is enabled.
![](/cf/files/e6c3766c-3674-4c06-ac7a-811e3fafa293)

**Step 4:** Enter template name that you have created in step 1 in Context Name.
![](/cf/files/5d011541-3b89-4cf7-806d-7c665d4dd678) 
 
**Step 5: ** Enter Route URL.
![](/cf/files/313275b4-09e3-4c83-9b00-f2cd31771347)


Steps to create group of routes:

**Step 1: ** Create new parent route.
![](/cf/files/ba120df8-9cd3-447b-8068-62f363d9d99f)

**Step 2: ** Create child route and select route name created in step 1 as parent route.
![](/cf/files/c1bc0488-b628-48b9-96d2-98305c726526)
![](/cf/files/4b9b0d54-f4d2-44d7-80be-a63fac673d97)

Example:
![](/cf/files/053515e0-21fc-457c-9a42-7049c03109ae)


Step to configure role based home page:

**Step 1:  **Create new role. Navigate to User management from home page.

**Step 2:  **Click on Manage Roles
![](/cf/files/c47b8aa7-3919-405a-84b7-01f572fc38d8)

**Step 3:  **Click on Add Role
![](/cf/files/94393d36-1bf8-4083-9d2c-16de4de16657)

**Step 4:  **Enter data in all mandatory fields and Role Priority should be unique. Click on save button
![](/cf/files/a29aac02-f32f-4753-8709-2f5f46e852cb)

**Step 5:  **Once role has been created navigate to Router.

**Step 6:  **Click on Define Home Page.
![](/cf/files/0eda2adf-7cf0-4dd0-95f3-77ed50af61ef)

**Step 7:  **Edit newly created role
![](/cf/files/23295c6d-0bab-47fd-872a-10214f944179)

**Step 8:  **Select Route Name.
![](/cf/files/2e52c447-9816-4436-9b3d-c369eba9b5b5)

**Step 9:  **Click on save button
![](/cf/files/3a611f48-4953-45b5-a7e4-89d0d9357f2c)

**Step 10:  **Now go back to User Management. 

**Step 11:  **Click on Manage Users.
![](/cf/files/535b61a7-7ade-46c0-903b-f8c3caf7c57a) 


**Step 12:  **Edit any user details.
![](/cf/files/d9051688-0939-493f-b822-0004858426c5) 

**Step 13:  **Enable newly created role and save the details
![](/cf/files/92ab2130-8770-4642-aec9-7919bb4cfa7d) 

** Validations: **
  
* Except for Root as context type, Module URL must be unique.
* If Include In Menu is enable and if Parent module is Root then Sequence must be unique at parent level or else if Parent module is other than Root then Sequence shoud be unique in that particular group. 
', 5, NOW(), 'admin@jquiver.io');


/****************************************************Router - End***********************************************************/



/****************************************************Grid Utils - Start**********************************************************/

REPLACE INTO jq_manual_entry(manual_entry_id, manual_id, entry_name, entry_content, sort_index, last_updated_ts, last_updated_by) VALUES
('7428a452-da97-4ef0-b6d7-acf4921beb82', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'Grid Utils', '# Grid Utils

Steps to create grid details.

**Step 1: -**
 ![](/cf/files/fec458a2-cc61-4ccb-890b-715f94930afe) 
 
**Step 2 :- **Add a new entry.
![](/cf/files/16afb46c-8249-4e46-975e-ad27250792f1) 

**Step 3 :- **Fill in the required details , Grid Table name can have table name, view name and Stored Procedure name as a value, and columns names are comma seperated values.
![](/cf/files/18d495df-c22f-4976-8b7d-82a114e650e9) 

**Step 4 :- **After creating the grid entry, go to templates modules from Jquiver home page and click on add template, a form will get opened, enter the template name in which the grid will be displayed and select the default-template-listing from Default Template drop-down, on selecting it will create the basic template for your grid listing page.
![](/cf/files/1fb8573a-5272-450e-9e1a-a88337c8c5a4)

**Step 5 :- **Modify the col models accordingly in template, and pass the appropriate values in the grid initialization. 

', 6, NOW(), 'admin@jquiver.io');

/****************************************************Grid Utils - End************************************************************/



/****************************************************Templating - Start**********************************************************/

REPLACE INTO jq_manual_entry(manual_entry_id, manual_id, entry_name, entry_content, sort_index, last_updated_ts, last_updated_by)
VALUES('17feffba-99f4-4591-9cb5-0fef46ee0b77', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'Templating', '# Templating 

Follow the steps below to create a Template dynamically.

**Step 1: **Visit the Templating module on Jquiver home.
 ![](/cf/files/afac4070-6d15-405e-9224-5fd54d17242e)
 
 **Step 2 : **Click on Add Template, it will open up a form to add/edit Template.
![](/cf/files/b0bfdd7e-3c31-4974-b22f-e103ab533d19)

**Step 3 : ** Give any Template name and choose the type of Template you want to create from the Default Template dropdown according to your requirement or you can simply write your code in the HTML editor.
![](/cf/files/ebaf8c2a-3932-48be-9418-616f94f5cebd)

**Step 4 : ** After entering the required details, save the Template.
![](/cf/files/aa882b04-dc5e-4ec3-a299-e3409195fff3) 
![](/cf/files/105451da-4d8c-4c3f-aab7-602a356c8613) 
 

**Step 5 : **After saving, the template will appear in the listing page as shown.
![](/cf/files/5d29a3c4-7013-4cab-bb59-ebb9af9321ec) 

**Step 6 : **We can edit the Template in the Edit mode.
![](/cf/files/ef715386-7f92-40a7-b950-c5a169bf3055)' , 7, NOW(), 'admin@jquiver.io');

/****************************************************Templating - End************************************************************/



/****************************************************Dynamic Form - Start********************************************************/

REPLACE INTO jq_manual_entry(manual_entry_id, manual_id, entry_name, entry_content, sort_index, last_updated_ts, last_updated_by)
VALUES('6938f0ac-00fe-4b94-95e7-02ef72016fe4', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'Form Builder', '# Form Builder 

**To add a new Dynamic Form follow steps: **  

**Step 1: ** Open Form Builder from home page.
![](/cf/files/70fb1426-4f15-485b-9c7f-d5680367ac44)

**Step 2: **Click on Add Form, it will open up a form to add/edit Form.
![](/cf/files/48ae2084-638c-4635-8ff0-e3566eef7076)

**Step 3:** After clicking on add , Enter form name, form description, and select Query Type (default value is Select Query) and the custom table around which form would be generated.
![](/cf/files/e06e6861-ba6c-463e-bb03-fea83ab2ca2d)

**Step 4:** The select Query Type dropdown will have the following values based on which the Select Script would be generated.
![](/cf/files/efe2818e-85a0-47c1-b4e9-161bbbceeb11)

**Step 5:** After selecting the table , Select Script , Html Script and Save Script would be populated.
![](/cf/files/e06e6861-ba6c-463e-bb03-fea83ab2ca2d)
![](/cf/files/d2b348d2-2917-4759-bd45-cfdda13d40bc)
![](/cf/files/0d534ed2-025d-43b4-bc30-d327b57185c6)

**Step 6:**In the Save Script Editor, the Query Type dropdown will have the following values based on which the save query can be written.
![](/cf/files/c1cbfbf9-d4d2-42aa-b016-8f78648d6702)

**Step 7:**After entering the values, save the form with required permissions.
![](/cf/files/42bab23c-c1b6-4db6-a9e5-370fa9877dd7)	
	 
**Step 8:** All the populated fields can be altered if required by clicking on the edit button in the listing page.
![](/cf/files/5cf017d5-e9c1-4bbb-bc8c-8f0a303c4aab)	

**Step 9:**The form can be edited in the edit mode and then changes can be saved further.
![](/cf/files/61c42c44-c8c4-4f91-b82a-1a6f0d3bf6b3)

**Step 10:**There is a provision for enabling Form IO by which the Form IO related form would be generated.
![](/cf/files/d94b3518-d835-4265-8699-aa3abc2583c3)

**Note:**For Form IO the select Query Type value by default will be JavaScript and the same table which was used in Form IO should be used to generate the Form for Form IO.
![](/cf/files/1b9ecc49-2ca8-4fa0-a686-b9aa0990c888)

**Step 11:** After selecting the table , Select Script , Html Script and Save Script for Form IO would be populated.
![](/cf/files/6ed4d9dd-b837-46d8-a03a-3720432861b9)
![](/cf/files/3b358b1b-bc49-4ef9-ae75-dfc9f6452dbe)
![](/cf/files/27f8447b-4e43-4715-ae48-e3b972c05942)

**Note:** For Form IO, detailed description is given in the Help Manual of Form IO.

**Note:** There is a provision for adding Script Library in Form Builder, detailed description of which is given in the Script Library Help Manual.
', 8, NOW(), 'admin@jquiver.io');

/****************************************************Dynamic Form - End**********************************************************/



/****************************************************Internationalization - Start********************************************************/

REPLACE INTO jq_manual_entry (manual_entry_id, manual_id, entry_name, entry_content, sort_index, last_updated_ts, last_updated_by) 
VALUES('9c25fb63-8336-4f22-bb97-a5042159d5c4', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'Internationalization', '# Internationalization

**Step 1: **Navigate to Internationalization.

 ![](/cf/files/f189cae9-b979-4525-b268-585cfd41c6e9) 

**Step 2: **Click on Add Label.

![](/cf/files/23cd6bae-7411-48a1-a0df-6251d46e90ad)

**Step 3: **Add new label and provide translation for all languages.

 ![](/cf/files/af7e0224-7882-4abd-98b1-8e2cfeb0080c)

Internationalization Demo

 ![](/cf/files/04643fd0-0f99-48e2-9cb6-585339a10fbd) 
 
**French language**

 ![](/cf/files/f1147460-9a8f-42da-8740-358ed18dd6b9) 
 
**Hindi languae**

 ![](/cf/files/8febc6d9-8cee-4c46-9776-96618679b4a3)

JQuiver provides few default resource bundle keys which application developer can utilize in their project like instead of JQuiver you can have your own project name in different template
Just update English language content for jws.projectName and it will be reflected in following templates
![](/cf/files/400b7c6d-b9cd-4d02-a7dc-258a3ce438c2) 
![](/cf/files/f86dd4f2-8e9c-4cea-b060-17e9a90f4595)
 
* Home page  - home-page (Title)
![](/cf/files/2b19fde4-4052-4795-add9-49d6f11f0876) 

* Login Page - jws-login
![](/cf/files/2a69ddbe-6cf6-4a0f-9968-824fee323719) 

* Reset Password Page - jws-password-reset-mail
![](/cf/files/e72998bd-d7c8-4d5a-a184-e2cb2a47cd5e) 

* Reset Password Page - jws-configure-totp
![](/cf/files/e4610ca0-516d-42f9-bd7a-ea2dee30884c) 

*Home Page - footer.
![](/cf/files/a9531ffe-e909-4d3a-98cf-34ce2aac2237)
 
**Import Export**
In this module, Import Export provision for Resource Keys is given. There are two buttons one for Import and other for Export.
 ![](/cf/files/206529b7-eba9-4004-b1c3-3ca93e8c0f1b) 
 
 *When we click on Export button, Resource Keys are exported in XML format .
 ![](/cf/files/425f0a7c-16d2-49ab-8484-6e14bb59d677)
 
 *Here is the sample file.
 ![](/cf/files/7174b451-be2d-486b-bbe1-5e27788654a9) 
 
 *When we click on Import button, an Import File dialog box opens .
 ![](/cf/files/2d8f96e4-5254-4741-ac6f-3f3666784f04) 
 
 *Here the corresponding file is chosen to be imported .
 ![](/cf/files/bf77f351-468f-4c4b-8af9-e68712589fb0) 
 
 *After importing, the Resource keys get imported in the application .
 ![](/cf/files/c37e67a1-aa74-4525-b42d-66a5c236b4d8)

', 9, NOW(), 'admin@jquiver.io');

/*****************************************************Internationalization - End*********************************************************/



/****************************************************REST API - Start************************************************************/

REPLACE INTO jq_manual_entry (manual_entry_id, manual_id, entry_name, entry_content, sort_index, last_updated_ts, last_updated_by)
VALUES('81c506ff-dab5-43de-a790-58af356de3e9', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'Rest API', '# Rest API 

Rest API lets you write API''s in below languages :
1. FTL (Freemaker)
2. Java
3. Javascript
4. Python
5. PHP

Follow the steps below to create a REST API dynamically.

**Step 1: **Visit the REST API module on Jquiver home.
 ![](/cf/files/6469c5d5-eb34-4199-90f2-caa40138dd57)

**Step 2 : **Click on Add REST API, it will open up a form to add/edit REST API
 ![](/cf/files/918a607b-753c-43d8-955c-383b70629fae)
 ![](/cf/files/570e9aad-b89b-4a03-b08b-07df318e99db)
 ![](/cf/files/1904c6f2-9e2f-401a-92ef-24f56e8698e2)
 ![](/cf/files/4738696e-a45d-436d-b9fc-8d0d582444ef)

**Available HTTP Method Type: **

* POST
* GET
* PATCH
* PUT
* DELETE
* OPTIONS
* HEAD
* TRACE

**Available HTTP Produce Type: **

* application/json
* application/octet-stream
* application/xml
* image
* text/html


**Available Platform Type: **

* FTL
* Java
* JavaScript
* Python
* PHP


**Type of queries: **

* Select Query
* Insert-Update-Delete Query
* Stored Procedure
* REST Client


Steps to create new REST API for FTL:

**Step 1: ** Provide unique REST URL
![](/cf/files/566052ac-cf76-49ce-b213-dfc384f74923)

**Step 2: ** Select appropriate HTTP Method Type
 ![](/cf/files/78a189bb-a01c-4916-89c5-4f7b97d39a58)

**Step 3: ** Select appropriate HTTP Produces Type 
 ![](/cf/files/c19d68cd-83ba-4888-928e-4518c3481b7c)

**Step 4: ** Select FTL as platfrom type and write service logic using FTL directives, expression etc.
 ![](/cf/files/13c874e0-0704-47f8-96a2-67a16b7df3f0)
 ![](/cf/files/0abe0a6d-a11f-428f-92e2-0c5b1219569b)

**Step 5: ** You can write any type of query. There are no restriction on number of queries.
![](/cf/files/0e132c56-a984-45da-b2d0-f84982970a02) 


Example to call and execute FTL REST API in any template: 
![](/cf/files/bfbf0453-5edf-496a-bbec-dee959a34ef7)


Steps to create new REST API for Java:

**Step 1: ** Provide unique REST URL
![](/cf/files/d9b6dd66-0f0a-40e6-8cef-15c6efa861f3)

**Step 2: ** Select appropriate HTTP Method Type
![](/cf/files/79e37ea0-6b50-45fd-97a2-dde0937b0b51)

**Step 3: ** Select appropriate HTTP Produces Type 
![](/cf/files/26d47021-c664-4d5f-aab9-89c5a0281a08)

**Step 4: ** Enter unique method name and that method with Method Signature must be present in Java file provided in service logic
![](/cf/files/57a80913-7368-4797-a46d-248797499a4d)
![](/cf/files/79f916c5-3d09-4443-ae5e-871ef468e1b1)

**Step 5: ** Select Java as platfrom type. You can have your own business logic in selected method. 

**Step 6: ** You can write any type of query. There are no restriction on number of queries.
![](/cf/files/92f68954-937a-4f66-b1b2-c0edf78c3d31) 
![](/cf/files/1144b319-c78f-48bf-b3ee-d39d7706ff08) 
![](/cf/files/f2e94f83-fe1b-4c10-accb-585c6dc78a5c)


Example to call and execute Java REST API in any template: 
![](/cf/files/87ed30d9-203f-45e2-b28a-3d2fe1393225)

Steps to create new REST API for JavaScript:

**Step 1: ** Provide unique REST URL
![](/cf/files/a98f62e8-7edf-497a-9f7e-d1ea80412e4c)

**Step 2: ** Select appropriate HTTP Method Type
![](/cf/files/cf5967b0-e7a9-4617-9796-5d09c618101f)

**Step 3: ** Select appropriate HTTP Produces Type 
![](/cf/files/7de7f045-464c-4833-81be-31a156e362ff) 

**Step 4: ** Select JavaScript as platfrom type and write service logic using JavaScript variable, functions, objects etc. Make sure to call your function after its declaration.
![](/cf/files/863e01cc-d650-4b38-8aae-5d9fd77ee9a9)
![](/cf/files/7ae6c1e4-b19e-462c-992a-5da74fa8d96c)

**Step 5: ** You can write any type of query. There are no restriction on number of queries.
![](/cf/files/86cfa8fe-54d3-449e-baf0-2b9e7958fd62) 
  
**How to handle custom exception messages**

Rather than providing an if-else condition on error handling part, we JQuiver support handling of exception handling messages using free marker.

Below is an example of how this can be achieved in this module. While creating a dynamic rest api, choose FTL as the platform and in Service Logic/Package Name section, you can provide the below FTL as per the requirement.

``` 
<#stop "This will contain the exception message to be rendered">
```
Once this is done, in the error handler of ajax call, check the responseText of xhr object. This will contain your exception message provided in the "stop" command.
i.e;
```
error : function(xhr, error){
			showMessage(xhr.responseText, "error");
},
```
This will display your message.

', 10, NOW(), 'admin@jquiver.io');

/*****************************************************REST API - End*************************************************************/



/****************************************************Autocomplete - Start********************************************************/

REPLACE INTO jq_manual_entry (manual_entry_id, manual_id, entry_name, entry_content, sort_index, last_updated_ts, last_updated_by) 
VALUES('5e46df00-e07a-4b73-889f-2894adfd3df8', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'AutoComplete', '# AutoComplete

 Follow the steps below to create a Autocomplete dynamically.

**Step 1: **Visit the TypeAhead/Autocomplete module on Jquiver home.
 ![](/cf/files/bf81338a-f15e-4cf7-97af-df7faa22b56b)
 
 **Step 2 : **Click on Add Autocomplete, it will open up to add/edit Autocomplete.
![](/cf/files/7643a091-d2c5-498e-8c20-33dc2952da0b)

 **Step 3 : **Fill in the Autocomplete Id and Description and choose the table for which the Autocomplete Query would be generated.
![](/cf/files/76c6d5b3-b55d-46cb-9ecf-6b101317fbbb)
**Note : **You can write the Autocomplete Query without choosing any table also.
 
 **Step 4 : **After filling in the required fields and giving required permission, save the Autocomplete page.
![](/cf/files/83d19871-84a3-479b-9a1c-293580538308)

**Step 5 : **The saved Autocomplete will appear in the listing page,which can be edited by clicking on the edit button.
![](/cf/files/8fd8f497-9fee-49f8-b785-5aea3fa8252a)

**Step 6 : **We can edit the Autocomplete in the Edit mode.
![](/cf/files/5083f06b-6d49-4eea-952c-0b7ef0373094)

**Step 7 : **In order to see the Demo Autocomplete, click on the Demo button.
![](/cf/files/3ecffe0c-1acb-44db-a179-bb59552d0244)

**Step 8 : **After clicking on the Demo button, this page is opened where Autocomplete default selected value is Action based on that the Autocomplete(Prefetch) is selected.
![](/cf/files/3e7c18be-2963-41b0-93fd-b5ebab0c24f7)

**Step 9 : **Then the Autocomplete(Local Storage) is selected.
![](/cf/files/22bb3522-f5ed-4609-a506-eca9172f78a6)

**Step 10 : **Then the Autocomplete(Clear Text) is selected.
![](/cf/files/6bab60b9-59ea-4b2b-9e3e-8363bc9fcfef)

**Step 11 : **A Multiselect Component can be included in this way.
![](/cf/files/912c7fd3-5de4-4d65-b0a8-c01bbb2c81fc)

**Step 12 : **A Multiselect (Dependent Multiselect) and Multiselect(Dependent Multiselect) can be included in this way.
![](/cf/files/7fe47d81-7327-40b7-943b-0a8380999ac2)', 11, NOW(), 'admin@jquiver.io');

/*****************************************************Autocomplete - End*********************************************************/



/****************************************************File Bin - Start************************************************************/

REPLACE INTO jq_manual_entry (manual_entry_id, manual_id, entry_name, entry_content, sort_index, last_updated_ts, last_updated_by) 
VALUES('8b425d39-72f0-4c6f-b71c-4ef247979538', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'File Bin', '# File Bin
File Bins play a pivotal role in managing the files using JQuiver application. We as a JQuiver application helps you to manage your files between different users and roles as per your requirement. Create File Bins for the application for uploading files. Users can include file bin in any form, template or module and upload multiple files of any format, size as per their requirements. There is a provision for Custom File Storage also.

**To add a new File Bin follow steps: **  

**Step 1: ** Open File Bins from home page.
![](/cf/files/d92d7c34-4d04-4ae4-b70d-5db94deeb1b4)

**Step 2: **Click on Add File Bin, it will open up a form to add/edit File Bin.
![](/cf/files/d3580ccd-8f4e-4ca1-b824-03fe6acad44f)

**Step 3:** After clicking on add , Enter File Bin Id, File Type Supported,Max File Size, No of files etc.
![](/cf/files/c70f4b62-d920-4624-a31b-b818eb7f1544)

For a File Bin there are different properties to be specified.

| Property Name | Usage |
| -------- | -------- | 
| File-bin-id     | Unique id, used to identify the file bins. This ids cannot be duplicate and it helps you to manage the files.     |
| File types supported     |Different types of file to be supported in that file bin. Providing specific extensions, helps you to add restrictions on the upload of file. If no restrictions has to be made, then you can add *     |
| Max file size      | This is the size of file which is to be uploaded. JQuiver has provided the conversion of file sizes between, bytes, kilo bytes and mega bytes. As per users understanding and comfortability, size can be provided. |
| No of files     | This will help to manage and provide a restriction for the user on file upload count.     |

There is a provision for providing Custom File Storage Class, for which the File Storage toggle button should be enabled.

Apart from the above properties, user can provide specific restrictions on Upload, View and Delete files in a file bin. To provide these restrictions, JQuiver has provided specific toggle buttons for all the three features.

Initially the Upload Validator, View Validator and Delete Validator toggle buttons would be disabled which means, no restrictions will be there for any of the operations on the files uploaded in that particular file bin. Depending on the requirements , the toggle buttons would be enabled.
![](/cf/files/3043e1da-ace2-41e0-80a9-c00bfc5e4f18)

If user wants to provide restriction on any of the functionality or all of the feature, then  the toggle buttons would be enabled and query has to be written as per users requirement.

We can provide restrictions for uploading a file. By default there wont be any restrictions. If we turn on this toggle button, then a default query will be generated. You can update the query as per your requirement.
Depending on the Query Type selected, the content of the Monaco editor would be changed. 
![](/cf/files/88434124-6d3a-4330-8c57-1bda0e623e7f)

For Javascript:
![](/cf/files/0c5893cc-8fe2-478c-8262-e11399c73e3b)

For Python:
![](/cf/files/38722876-59a2-4f23-beca-6812444a2aaf)

For PHP:
![](/cf/files/843ab443-b45b-457b-9eb5-b539af54bf87)

The query should return data with alias "isAllowed". If value of isAllowed is 1, then upload will be allowed. If it is 0, then upload will be restricted.
 
**View**
As explained above, We can provide restrictions for downloading a file uploaded as well. By default there wont be any restrictions. If we enable the toggle button then the query will be generated. You can update the query as per your requirement.
The query should return data with alias "isAllowed". If value of isAllowed is 1, then view/download will be allowed. If it is 0, then view/download will be restricted.
 ![](/cf/files/fe297477-dd0a-4647-8844-dc65c2356d3b)

 **Delete**      
Using this, restriction criteria can be provided for deleting a file. By default there wont be any restrictions. If we enable the toggle button, then a default query will be generated. You can update the query as per your requirement. 
The query should return data with alias "isAllowed". If value of isAllowed is 1, then delete will be allowed. If it is 0, then delete will be restricted.
![](/cf/files/39cbb1c3-7472-4e12-9748-6400bf7a2fec)

In the above example of delete query, we are providing restriction on deleting files as per owner. That is, user can delete files, uploaded by them. They wont have rights to delete files uploaded by any other user. Whereas, Admin will have right to delete all the files, irrespective of the file owner.

 By default JQuiver has provided four File Bins as shown in the image below.
![](/cf/files/bf72927a-0a50-466b-b284-0854bc012e85)

 1. default :- Default usage. All the Files will be uploaded on the Common Files.
 2. helpManual :- This file bins manages the files specifically for JQuiver Help Manuals		
 3. profilePic:- This file bin is used for uploading the profile pic of the user.
 4. mailAttachment:- This file bin is used for attaching files in the mail.
 
For uploading files, in the Common Files section click on the Common Files icon as shown in the picture:
![](/cf/files/28e77767-8fad-4d46-a126-666413aed318)

In this section, as is shown in the picture below you can upload files if you are an Admin, Anonymous user wont be able to upload files. Here default file bin is used.
![](/cf/files/a2da1bf7-67cf-4ec0-96ef-d89604f61003)
![](/cf/files/f1968fb9-559b-47fd-bdc7-07e6b3453263)

There is also a provision of adding Script Library in the File Bins. For creating a Script Library you need to click on the Script Library icon as shown in the image below:
![](/cf/files/69b75a97-fe0b-460e-ab0d-116821a30727)

For including Script Library in File Bins, there is a button provided upon each validators which is initially disabled.
Script Library button would be available only for Javascript, Python and PHP.
![](/cf/files/aeda35e3-bd33-4485-bba8-c43c35693f51)

Details about Script Library is provided in the Script Library Help Manual.

 
', 12, NOW(), 'admin@jquiver.io');

/*****************************************************File Bin - End*************************************************************/



/****************************************************Dashboard - Start***********************************************************/

REPLACE INTO jq_manual_entry (manual_entry_id, manual_id, entry_name, entry_content, sort_index, last_updated_ts, last_updated_by) VALUES
('3f0f6b4e-9a00-4b89-9a64-415a1f8256d2', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'Dashboard', '# Dashboard

A dashboard is a page that consists of multiple components, mostly independent of each other. Dashboards are reusable, customizable, can display different types of information at one place. It is an information management tool that receives data from a linked database to provide data visualizations. Dashboard use data visualization to help businesses make real-time decisions. They help to create the daily reporting, application usage, trends dashboard for web application and controls it with the dashboard admin panel. Dashboards can help ensure that everyone has access to the latest data and insights, which can lead to better decision-making. 
Dashboards can be customized, which allows different people within an organization to use the same data in different ways.  
Dashboards can display information in real-time, which can help decision makers act quickly.

Steps for creating a Dashboard are as follows:

**Step 1: **Navigate to Dashboard from the home page.

![](/cf/files/47c77f23-d06b-4ff2-9991-d60db2ae9fd0) 

After we need to create Dashlets to include in the Dashboard.
# Dashlet
-A dashlet is an individual component that can be added to or removed from a dashboard. They are a reusable unit of functionality. When the dashboard loads it should just show the UI of the dashlets . x
After the page load each dashlet sends a server call (based on its type) to fetch its data. After the data has been fetched, each dashlet (based on its type) renders the data. Dashlets render the information in UI that needs to be displayed through the dashboards . Information can be of various types like Server Listing, CPU Usage etc. as per the user’s requirement. 
Dashboards are containers consisting of multiple dashlets. They control the way the dashlets are organized in dashboards.

Steps for creating a Dashlet.

**Step 1: **Click on Manage Dashlets after navigating to Dashboard page as shown in the picture below.
![](/cf/files/fd119b35-a6fe-476d-9561-46b2a7c90341)

**Step 2: **Click on Create New Dashlet

![](/cf/files/51931155-0c10-43e9-bd3d-4bde6aee0431)

**Step 3: **Create New Dashlet by providing all necessary information

![](/cf/files/b19e9cbb-b05a-4e29-a398-0feff561a0e4)
![](/cf/files/65fc7e48-cda1-4661-8437-dea3533c4a54)
![](/cf/files/8e530a09-c3b1-4197-a49e-3d220f4e063e)
![](/cf/files/fc0c2781-92fe-4879-8dab-ce0a893b40b8)

**Step 4: **Navigate back to Dashboard listing page and click on copy button to copy the dashlet Id.
![](/cf/files/3b7ea334-d513-4827-bd5a-4ff88f08d571)

**Step 5: **You can edit the dashlet in the edit mode.
In order to make the dashlet active, you need to enable the Dashlet Active toggle button and for including header, you need to enable the Dashlet Header toggle button as shown in the picture below.

![](/cf/files/cbeb00be-4b05-4d77-89b6-8242b85eb3ac)

**Step 6: **User can also add provide additional configurable properties to a dashlet like no of records to be visible at a time or you can allow end user to enter a name or age which will be taken into consideration while fetching records. User also need to provide HTML content and SQL query.

![](/cf/files/0898958c-8cce-4ac3-8f6f-1ce5b886acfe)

**Step 7: **Navigate back to Dashboard listing page and click on Create New Dashboard button for creating a dashboard.
	
 ![](/cf/files/45ab6497-5953-4120-b05b-ea9365ebe648)

**Step 8: **Create new dashboard by providing all neccessary information and also include the dashlet created before.
User has an option to make a dashboard draggable by enabling the Draggable Dashboard toggle button.
User can also include Dashboard header based on his/her requirement.

![](/cf/files/60905a88-b892-4b9b-9e9c-0b4682ebef9a)

**Step 9: **Dashboard created will be displayed in the listing page.

![](/cf/files/f6088a79-2a9a-428f-bf41-955bf8b72f19)

**Step 10: ** By clicking on the eye icon of the dashboard in the listing page , user can view the dashlet included in the dashboard as shown below.

![](/cf/files/588f22d8-ea20-4345-b825-e564444a8322)

**Step 11: **By clicking on the arrow button of the displayed dashlet user can refresh the dashlet in every desired secs/minutes depending on the requirement. This will automatically refresh the dashlets based on the time set.

![](/cf/files/8fce9b14-8a4e-491e-b8e0-100462515b40) 
![](/cf/files/75db071a-43e2-4578-b0a4-38c919a3bca6)
![](/cf/files/8e66f0dd-5d85-4b93-b8a6-e045f19435a7)

**Step 12: **By clicking on the filter button of the displayed dashlet user can modify the configurable properties  and save the changes as shown in the picture.

![](/cf/files/197e8754-a4d2-4265-a322-cbed789324c0)
![](/cf/files/f71f5cfb-0457-4b62-9177-5d223ff95aaa)

**Step 13: ** For displaying the configurable properties in the dashlet the following changes have to be done in the HTML editor of the dashlet.
![](/cf/files/58c9e26e-c4ca-4eb4-90e2-a4d740545c5a)

**Step 14: ** After making the changes, the changes will be reflected in the displayed dashlet.

![](/cf/files/7d339fde-6e6f-47c3-bd25-46b98eaa96bb)

**Step 15: ** There is a provision also for the user to change the value of the configurable properties from the console by calling refreshDashletContent method and passing the values to be changed as shown in the picture. For this user needs to provide the dashlet id which he/she can get by clicking on the copy button of the required dashlet in the listing page.

![](/cf/files/32494c32-ddcf-4d77-b621-c1fdcc7449ad)
![](/cf/files/3a946afd-e5c8-477a-bbf8-89f7e4ea6099)


', 13, NOW(), 'admin@jquiver.io');

/*****************************************************Dashboard - End************************************************************/



/****************************************************Versioning - Start**********************************************************/

REPLACE INTO jq_manual_entry (manual_entry_id, manual_id, entry_name, entry_content, sort_index, last_updated_ts, last_updated_by)
VALUES('1d5577fa-a7e3-4bdf-85ed-b84ac81fbb0b', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'Versioning', '# Versioning

Versioning plays pivotal role when developing an application which require frequent changes and keeping track of previous version is very important. You just need to add DBUtils as a dependecy in your application to enbale versioning for your application. Modify **max-version-count** from **Application Configuration** module if you want to change default max no of versionig that should be saved in database.

To save data in versioning table, follow the steps below:
**Step 1: **Autowired ModuleVersionService
@Autowired
private ModuleVersionService moduleVersionService				= null;

**Step 2: **Save data by following line of code
moduleVersionService.saveModuleVersion(entityData,null, primaryKeyValue, entityName, sourceTypeId);

**Enable Revision Details On UI: **
**Step 1: **To access revision history of any module add following code in your listing page:
**HTML structure:**
```
<form action="contextPath/cf/cmv" method="POST" id="revisionForm">
 	<input type="hidden" id="entityName" name="entityName" value="entityName">
    <input type="hidden" id="entityId" name="entityId">
	<input type="hidden" id="moduleName" name="moduleName">
	<input type="hidden" id="moduleType" name="moduleType" value="moduleType">
	<input type="hidden" id="saveUrl" name="saveUrl" value="saveUrl">
	<input type="hidden" id="previousPageUrl" name="previousPageUrl" value="previousPageUrl">
</form>
```

**Step 2: **Modify your action column formatter function and add revision button:
`<span id="primaryKeyValue_entity" name="moduleName" onclick="submitRevisionForm(this)" class= "grid_action_icons"><i class="fa fa-history"></i></span>`

**Step 3: **Add submitRevisionForm function
**JS changes:**
```
function submitRevisionForm(sourceElement) {
	let selectedId = sourceElement.id.split("_")[0];
	let moduleName = $("#"+sourceElement.id).attr("name")
    $("#entityId").val(selectedId);
	$("#moduleName").val(moduleName);
    $("#revisionForm").submit();
}
```

**Simple example :**

**Step 1: ** Click on revision button from the listing page: 
![](/cf/files/c1052fca-406c-473d-8d26-19a6cb1641e1) 


**Step 2: ** Select revision time using autocomplete:
![](/cf/files/20407136-778d-4237-a355-dcdbefc74703)


**Step 3: ** See the difference between two versions
![](/cf/files/a15256c3-7bec-4577-9c15-f9d8668ca68f)
![](/cf/files/915c16d4-7de6-4026-87ee-4ab39b2b4c19)


**Step 4: ** If you want you can replace current data with old data using copy button and then save the changes.
 ![](/cf/files/aa99668a-7aba-4d09-8c04-af127289db1f) 
 ![](/cf/files/8961a88b-be30-452e-9ae7-314edf76e340)


**Complex example:**

![](/cf/files/1e44e825-7d8d-4485-8e59-20a74888106b)
 
![](/cf/files/1c012d8f-750d-4af1-aa07-0dab31cd50ac)

![](/cf/files/0c402fca-cf3a-41dd-8693-56156e5cab67)', 14, NOW(), 'admin@jquiver.io');

/*****************************************************Versioning - End***********************************************************/




/************************************************Import Configuration - Start***************************************************/

REPLACE INTO jq_manual_entry (manual_entry_id, manual_id, entry_name, entry_content, sort_index, last_updated_ts, last_updated_by)
VALUES('be37c240-2607-4d79-9ef1-136dbd7c524b', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'Import Configuration', '# Import Configuration

Importing the configuration plays pivotal role when you want to migrate some configuration or data from an existing database to another database. 

To import a configuration browse and upload the particular zip file already exported and saved in the local drive. Once uploaded, all the configuration to be imported will be listed down with the existing version number and import version number. Existing version number is the number which is available in your current setup, if the configuration already exists and import version number will be the number of the configuration from the setup from which it was exported.
The configuration can imported all together or can be be imported one by one after review, by the user. This can also be compared, with the existing version in the system, if any, using the comparison icon (if enabled).

**Step 1: ** Go to Home page and click on the Import Button.

![](/cf/files/7a1e3f09-d3dd-4e39-bdf2-3d5003e551be)

**Step 2: ** Import Configuration page is opened, where the zip file is selected for import.

![](/cf/files/8a277a0a-aa06-45db-aa3d-44a8cbbafa25)

**Step 3: ** By clicking on the import button the files from the zip gets listed in the grid.

![](/cf/files/8f8c8daf-8538-4168-ad71-e31ac523d8ef)

By clicking on the download button, the individual file gets imported and by clicking on the Import All button all the files in the list gets imported.

**Step 4: ** Comparison of the imported configuration is done by clicking on the comparison button and can be viewed as in versioning module.

 ![](/cf/files/ae754fd5-89ac-48a1-8248-5b62aba77384)
 
 **Step 5: ** After the comparison is done, the changes are copied and then saved successfully.
 
', 15, NOW(), 'admin@jquiver.io');

/************************************************Import Configuration - End*****************************************************/




/************************************************Export Configuration - Start***************************************************/

REPLACE INTO jq_manual_entry (manual_entry_id, manual_id, entry_name, entry_content, sort_index, last_updated_ts, last_updated_by)
VALUES('dd97c23d-feef-4cea-afcf-3cece7819159', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'Export Configuration', '# Export Configuration

Exporting the configuration plays pivotal role when you want to migrate some configuration or data from an existing database to another database. 

**Step 1: ** Go to Home page and click on the Export Button.

![](/cf/files/f068fa05-6f66-4272-aeb1-f206bbee7bef)

**Step 2: ** In the Export Module Page, by default all Custom Modules are displayed and all modules are selected and system configurations deselected. 

![](/cf/files/a7097cba-a7c5-4f69-a514-30336afe5e19)

**Step 3: ** By checking the check box of Deselect button , all the modules are deselected.If at all user wants to reset the default selection and export only particular configuration, then that can be achieved by "Deselect All" checkbox.

![](/cf/files/8ba2ceea-21fb-4810-a6f3-c546cabf6f50)

**Step 4: ** Custom option can be also changed from the Show drop-down.

![](/cf/files/9189c80b-0151-4d87-9eed-0911ff2c6694)

**Step 5: ** To export a configuration go to particular module on the export configuration page and select the same.By checking the Permission check box, the permission for the same modules can also be exported in the same zip. 

![](/cf/files/0348a0e1-ac68-49eb-a0f5-db241e512028)

**Step 6: ** Once the configuration to be exported are finalized, go to the preview page using "Next" button and get it exported in zip format.

![](/cf/files/f870d845-5f40-4ee0-b20e-585eea560045)
 
**Step 7: ** By clicking on the Export Zip button, the files are imported in the zip format.
 
![](/cf/files/13685dc4-5866-4f91-a927-95e28b39c3e3) ', 

16, NOW(), 'admin@jquiver.io');

/************************************************Export Configuration - End*****************************************************/




/************************************************Dev Environment - Start********************************************************/

REPLACE INTO jq_manual_entry (manual_entry_id, manual_id, entry_name, entry_content, sort_index, last_updated_ts, last_updated_by) 
VALUES('935b9394-c33d-4113-a248-27c46c45e7e9', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'Dev Environment', '# Dev Environment:

You can modify the following modules in your favourite editor just by changing the value of **Profile** property to **dev** from application configuration:
* Templating
* Dynamic Form
* Dashlet
* Rest API

![](/cf/files/5332fa07-f42e-4b40-ad30-bb0ffbad979b)

You can get the default path from the property_master table or from UI Application Configuration,which will be used by the application to download, upload and process the content

![](/cf/files/a8956dc5-1b52-4a6f-b326-2c757dc7e850)

![](/cf/files/d73c1cc1-ec63-49cd-a79c-1891e88de41b)

Dev environment features:
** Download: **If you want to download(from database to local directory) all custom records then you can use Download All feature.
** Upload:**If you want to upload(from local directory to database) all custom records then you can use Upload All feature.
** Note: **If you add any new record from the UI then application will save the changes in database as well as create appropriate files in local directory.
 
Download All custom templates:
![](/cf/files/58d80ab5-4eae-43da-b61e-d1c5bbd54f86) 
	
Download All custom dynamic forms:
![](/cf/files/88950dfd-c8c7-48bf-bbad-6a8a8df67209)

Download All custom dashlets:
![](/cf/files/5c3bb528-a63e-4ad0-95d1-863d755f3215)

Download All custom Rest APIs:
![](/cf/files/c9498a41-8921-44b0-a5af-7743d7e659e5)

Upload All custom templates:
![](/cf/files/ce49087d-106f-4bdc-bd67-5aa71e619b90)
 
Upload All custom dynamic forms:
![](/cf/files/88950dfd-c8c7-48bf-bbad-6a8a8df67209)
 
Upload All custom dashlets:
![](/cf/files/a556eeae-82ce-4c30-9342-a715a9e28c33)

Upload All custom Rest APIs:
![](/cf/files/b171c780-199c-443b-adf5-5166c604e2e8)

Download/Upload templates:
![](/cf/files/0141f87a-4a0f-4cb3-9456-3a9487b50b68)

Download/Upload dynamic forms:
![](/cf/files/109cbd35-3656-489a-a8d8-d2837b8160ca)

Download/Upload dashlets:
![](/cf/files/d3816cab-ce34-4c70-a3ad-aa2277f6198) 

Download/Upload Rest APIs:
![](/cf/files/90c87a4f-743c-4a56-8118-6346a531aead) 

Find all the templates under **Templates** folder. It consist of **template-name.tgn** files

![](/cf/files/ff40843a-54a2-4d5d-8923-2a9a01ab871b)
 
 Application will save all forms in **DynamicForms** folder. Inside DynamicForms, there will be one folder for each dynamic form and it should contain at least 3 files:
*  htmlContent
*  selectQuery
*  saveQuery-1

![](/cf/files/248ad2c0-2ab0-4a44-81bc-df9accc4ca36)

![](/cf/files/18c9fdd3-3d7e-4d7c-8874-6e31ea9a9aef)

 Application will save all Rest APIs in **DynamicRest** folder. Inside DynamicRest, there will be one folder for each Rest API and it should contain at least 2 files:
*  selectQuery-1.tgn
*  serviceLogic.tgn

![](/cf/files/00c75941-bcd3-4fe4-92e7-f12503dee6fc)

![](/cf/files/299b9053-0616-4f52-a9ec-2280469c361e)

**Note: **You can add as much selectQuery as per your requirement.

Like dynamic forms, each dashlet details will be stored in seperate folder and all dashlets will be saved in Dashlets folder. There should be only two files in each folder:
* htmlContent
* selectQuery

![](/cf/files/acccd191-3554-4f60-92e0-5ff945ac29bd)

![](/cf/files/0390d488-bd3a-4422-b901-61d33d4313a3)


', 17, NOW(), 'admin@jquiver.io');

/************************************************Dev Environment - End**********************************************************/



/*************************************************Others - Start****************************************************************/
REPLACE INTO jq_manual_entry (manual_entry_id, manual_id, entry_name, entry_content, sort_index, last_updated_ts, last_updated_by) 
VALUES('50f92287-c438-4ee2-bdfa-b4e710d2e64b', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'Others', '# Others
There are few other features, which we support in JQuiver. Below we have listed them in detail.

**Validation Messages**
To add any validation message at javascript level, we have provided 4 types of message criteria.
Success, Info, Warning, Error.
As per user''s requirement different messages can be added as follows.

showMessage(messageText, "success");
showMessage(messageText, "info");
showMessage(messageText, "warn");
showMessage(messageText, "error");

This will show the validation message, at the right top of browser with specific depiction, as below.
![](/cf/files/7bba0ef5-1416-4d3c-84bc-a48e2d0777ec)

**Favorites**
For navigation from one page to another, JQuiver has provided an option called Favorites. This will be available in right top of a functionality.
If you mark the page as a Favorite listing, then the page will be listed down in the Favorite list and you will be allowed to navigate between these page directly, rather than accessing the page from home.
 ![](/cf/files/9674702e-c1e5-4a67-8f98-f167871a2207)
 
To make it precise, say if you have to move from one template to a form in Form-builder. 
In normal case, you will have to go back to Home page from template listing, access the Form builder module, search for the form and open it.

But Favorite helps to direct navigation.

**Step 1:**
Add the page as a Favorite
 ![](/cf/files/11363af4-6efe-4e18-95dc-8ed6c83033d4)
 
 Now go to another module, say Form Builder, any form
  ![](/cf/files/881f45cd-2e96-4c62-8b0e-65bc2925dd0b)
	
	If you see the welcome template added as favorite will be listed in the listing, which will help you to navigate directly to the desired page.
	
	Favorite feature is applicable for
	1. Grid Utils
	2. Templating
	3. TypeAhead/Autocomplete
	4. File Bins
	5. Form Builder
	6. Rest API
	7. Dashboard
	8. Dashlet
	
**Calendar**
There can be many a conditions, that you need to add to a calendar component available in JQuiver.
Following are some, which we are explaining how to handle.

If you have calendar in any of your form or template, then following has to be taken care.

**Step 1:**
Import following js into your form/template

```
<script type="text/javascript" src="${contextPath!''''}/webjars/1.0/JSCal2/js/jscal2.js"></script>
<script type="text/javascript" src="${contextPath!''''}/webjars/1.0/JSCal2/js/lang/en.js"></script>
```


**Step 2:**
Below code snippet has to be added, to define the calendar component. This will allow you to define the date format, time format, input field on which calendar is declared etc.
```
fromDateCal = Calendar.setup({
			trigger    : "validfrom-trigger",
			inputField : "validfrom",
			dateFormat : "%d-%b-%Y",
			weekNumbers: true,
            showTime: 12,
            min : Calendar.dateToInt(new Date()),
			onSelect   : function() { 
				let selectedDate = this.selection.get();
				let date = Calendar.intToDate(selectedDate);
				date = Calendar.printDate(date, "%d-%b-%Y %H:%M:%S");
				$("#"+this.inputField.id).val(date);
				this.hide();
                if(fromDateCal.selection.sel <= Calendar.dateToInt(new Date())){
                    tillDateCal.args.bottomBar = true;
                }else{
                    tillDateCal.args.bottomBar = false;
                }
                tillDateCal.args.min = Calendar.intToDate(fromDateCal.selection.sel);
                tillDateCal.redraw();
                $("#errorMessage").hide();
			},            
		});
```

In the above example, we have also explained how to set any value on the calendar, on select of any other criteria. That is, if you want to preset a default value on the calendar, depending on the data input on another field dynamically, it can be handled in  "onSelect".

**Step 3:**
Define the ui component on which the calendar has to be set. make sure to link the id properly.
```
<label for="validfrom">Valid from</label>
<span>
			<input id="validfrom" data-type="varchar" onkeyup="validateFromDate()" name="validfrom" class="form-control" placeholder="Valid from" autocomplete="off"/>
			<button id="validfrom-trigger" class="calender_icon"><i class="fa fa-calendar" aria-hidden="true"></i></button>
</span>
```

**Step 4:**
Provide validations, if required as below
```
function validateFromDate(){
        let currentDate = Calendar.printDate(new Date(), "%d-%b-%Y %H:%M:%S");
        let fromDate = Calendar.intToDate(Calendar.parseDate($("#validfrom").val()), false);
        let fromDateInt = Calendar.dateToInt(fromDate);
        if(isEdit == 0 && fromDateInt < Calendar.dateToInt(new Date())){
            $("#errorMessage").html("Valid from must be greater than " + currentDate);
            $("#errorMessage").show();
            return false;
        }
        $("#errorMessage").hide();
        return true;
}
```

# Keyboard Shortcuts
**Monaco Editor Shortcuts**

**Ctrl + S**  Save editor content
**Ctrl + Shift + M**  Editor Enter/Exit Full Screen
Above shortcuts are available in following modules:

* TypeAhead/Autocomplete
* File Bin
* Templating
* Form Builder
* REST API
* Dashlet

', 18, NOW(), 'admin@jquiver.io');

/*************************************************Others - Start****************************************************************/



/*************************************************Help Manual - Start***********************************************************/

REPLACE INTO jq_manual_entry (manual_entry_id, manual_id, entry_name, entry_content, sort_index, last_updated_ts, last_updated_by) 
VALUES('4dddd5a0-d69f-4e6c-bd91-0fa10e8adbfb', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'Help Manual', '# Help Manual

Help Manual module helps us to understand the application. In this module we can store all the documentation related to the application, as per users need.
By default we have provided, JQuiver Help Manual whichc helps you to understand the JQuiver application, its features and functionality support and how to use JQuiver to build your own application.

To create/view a help manual, go to Help Manual option from the main page.
We support custom and system help manuals. Jquiver Help Manual is a system help manual, which explains you about the JQuiver application.
All other user created help manuals are custom type manuals.
 ![](/cf/files/c8311426-74b8-4811-9319-37a04c47c598)

To create a new manual, click on "Create New Manual" button. This will help you to create a new custom manual. Every manual has role based access.
As per the role assigned, manuals can be viewed and edited. If a manual has no "Authenticated" permission, then it will not be available for Authenticated users.
 ![](/cf/files/5831d7d7-d16c-4048-9bf1-d2ee1fdc127c)
 
 Once a manual is created, click on the second action button, to add entries/data into the manual
Different sections in manuals will be displayed as different tabs, as shown below. You can assign the ordering of the tabs while creating an entry. Every tab/section will be created as a "Entry", by clicking on "Create New Entry" button.
 ![](/cf/files/0f684eec-041c-4e6e-8e69-025e487d3356)
In this page, you can enter the "Entry Name", "Display Index", "HTML Content", files/images to be displayed.
Display Index will help you for the ordering of the entry. As per the index, the entry will be displayed in that particular ordered tab.
The number of files to be uploaded in the entry depends upon the configured file count in file bin.

**Note:-** No file will get uploaded unless the manual entry is already available in the DB. Make sure you save the manual entry first without any file and then edit it to upload the files/images.

Once the manual entries are made, using "View Manual", you can view the manual as below.
 ![](/cf/files/b71e324a-4cd8-48e9-9740-fd792e9c7810)
 
 Your manual can be viewed either from "Help Manual" module, or you can provide an entry in Router to accesss you manual from menu or any other location in your application.
 
 **JQuiver Developer Guide**
 
 While adding new manual, following entries in database will get modified.
 A new entry in jq_manual_type is created.
 Then as per the new manual entries, data is updated in jq_manual_entries. A foreign key constarint will be there in this table, with the manual_id in jq_manual_type.
 If any file is uploaded in the manual entry, then data entry will be made in jq_file_upload.
 In jq_file_upload, file_association_id will be the manual_entry_id from jq_manual_entries. Other than that, it will have the file_bin_id which is from jq_file_upload_config table, then the location where the image is available. The file location can be configured in Application Configuration module.
 Apart from this, it will also have the physical_file_name, which will be the name in which the file will be available in the specified location. ',
  19, NOW(), 'admin@jquiver.io');


/*************************************************Help Manual - End*************************************************************/


REPLACE INTO jq_file_upload (file_upload_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_bin_id, file_association_id) VALUES
('40281694762864a50176288c66620001', '/images', 'REST_API_Master.PNG', '54d81128-66eb-44e1-bd7c-a7aba6c1f081', 'admin@jquiver.io', NOW(), 'helpManual', '81c506ff-dab5-43de-a790-58af356de3e9'), 
('40281694762864a50176288c755a0002', '/images', 'Add_Edit_REST_API_Step_1.PNG', 'f34047da-dbe7-4f0a-8672-8ff74a47b391', 'admin@jquiver.io', NOW(), 'helpManual', '81c506ff-dab5-43de-a790-58af356de3e9'),
('402816947628cb18017628cfa6110000', '/images', 'Add_Edit_REST_API_Step_2.PNG', '538e5a16-0eb6-47d3-b196-23733426a0e1', 'admin@jquiver.io',  NOW(), 'helpManual', '81c506ff-dab5-43de-a790-58af356de3e9'),

('402816947628e22d017628e709eb0000', '/images', 'Dashboard_Master.PNG', '1a999d28-8f4b-4475-99e6-439e7b40bec4', 'admin@jquiver.io',  NOW(), 'helpManual', '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'), 
('40289d3d7650c0a1017650c8fccf0001', '/images', 'Add_Edit_Dashlet_Step_1.PNG', '5eda819c-bcbb-486a-b94b-1d93d73379c5', 'admin@jquiver.io',  NOW(), 'helpManual', '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'), 
('40289d3d7650c0a1017650c90cce0002', '/images', 'Add_Edit_Dashlet_Step_2.PNG', 'e54568fc-8d7c-40eb-ab62-cd8521956878', 'admin@jquiver.io',  NOW(), 'helpManual', '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'),
('402816947628e22d017628e71f020001', '/images', 'Add_Edit_Dashlet_Step_1.PNG', 'ff8c8d67-2b68-4a2b-843b-de7d747324b7', 'admin@jquiver.io',  NOW(), 'helpManual', '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'), 
('402816947628e22d017628ea39e00002', '/images', 'Add_Edit_Dashlet_Step_2.PNG', '75368f52-d90f-456c-b9f5-69703014991e', 'admin@jquiver.io', NOW(), 'helpManual', '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'),
('40289d3d7650c0a1017650c8ef410000', '/images', 'Add_Edit_Dashboard_Step_1.PNG', '8346ff39-89b4-40f2-b1fa-cd01ca503541', 'admin@jquiver.io', NOW(), 'helpManual', '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'),
('40289d3d7629c275017629c35c830000', '/images', 'Add_Edit_Dashboard_Step_2.PNG', '606e1003-2f89-4514-9b63-1ff0d11ac8ce', 'admin@jquiver.io', NOW(), 'helpManual', '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'),

('40289d3d7629c27501762a0eb728000a', '/images', 'Module_Revision_Enable_UI.PNG', '461685d6-a188-406c-920e-41bc210ada68', 'admin@jquiver.io', NOW(), 'helpManual', '1d5577fa-a7e3-4bdf-85ed-b84ac81fbb0b'), 
('40289d3d7629c27501762a0ee8f3000b', '/images', 'Module_Revision_Simple_Example_1.PNG', '5303697a-2fc2-4abc-af89-9621fe0fdf25', 'admin@jquiver.io', NOW(), 'helpManual', '1d5577fa-a7e3-4bdf-85ed-b84ac81fbb0b'), 
('40289d3d76509b1e017650ae7d4d0015', '/images', 'Module_Revision_Simple_Example_2.PNG', 'af8ce88f-a5f0-401d-ac19-05fb1e583d15', 'admin@jquiver.io', NOW(), 'helpManual', '1d5577fa-a7e3-4bdf-85ed-b84ac81fbb0b'),
('40289d3d7629c27501762a0f3e5a000c', '/images', 'Module_Revision_Simple_Example_3.PNG', 'ed8c766f-5da2-4dbe-a335-c3aeca501157', 'admin@jquiver.io', NOW(), 'helpManual', '1d5577fa-a7e3-4bdf-85ed-b84ac81fbb0b'), 
('40289d3d7629c27501762a0f8466000d', '/images', 'Module_Revision_Complex_Example_1.PNG', '4713e718-d56d-46f4-9d98-fb0c74902c4a', 'admin@jquiver.io', NOW(), 'helpManual', '1d5577fa-a7e3-4bdf-85ed-b84ac81fbb0b'), 
('40289d3d7629c27501762a0f914b000e', '/images', 'Module_Revision_Complex_Example_2.PNG', '44c79c88-138d-487e-ab18-187c55efff6c', 'admin@jquiver.io', NOW(), 'helpManual', '1d5577fa-a7e3-4bdf-85ed-b84ac81fbb0b'),
('40289d3d7629c27501762a0f9c51000f', '/images', 'Module_Revision_Complex_Example_3.PNG', '1de987bc-dbc4-46f3-9eb4-d524304c5fe4', 'admin@jquiver.io', NOW(), 'helpManual', '1d5577fa-a7e3-4bdf-85ed-b84ac81fbb0b'),

('40289d3d7629c27501762a22257f0012', '/images', 'Grid_Util_Master.PNG', '5a25ffc8-d516-41ca-931e-6a3ad8ddf6bf', 'admin@jquiver.io', NOW(), 'helpManual', '7428a452-da97-4ef0-b6d7-acf4921beb82'), 
('40289d3d7629c27501762a22341b0013', '/images', 'Add_Grid_Details_Step_1.PNG', 'dbbaf3ee-eb76-4e9c-8df4-27e8be86cab2', 'admin@jquiver.io', NOW(), 'helpManual', '7428a452-da97-4ef0-b6d7-acf4921beb82'), 
('40289d3d7629c27501762a223f2e0014', '/images', 'Add_Grid_Details_Step_2.PNG', '2cb34a18-ad8c-4ace-818f-d7d3eb0d5d5d', 'admin@jquiver.io', NOW(), 'helpManual', '7428a452-da97-4ef0-b6d7-acf4921beb82'), 
('40289d3d7629c27501762a2258450015', '/images', 'Default_Listing_Template.PNG', '165dad67-09bf-4521-b4cf-023f7851516f', 'admin@jquiver.io', NOW(), 'helpManual', '7428a452-da97-4ef0-b6d7-acf4921beb82'),

('40289d3d762c323c01762cc6c8a30000', '/images', 'Multilingual_Master.PNG', '263dbcbd-754e-42d0-b1bf-3ba1dfa85a0e', 'admin@jquiver.io', NOW(), 'helpManual', '9c25fb63-8336-4f22-bb97-a5042159d5c4'), 
('40289d3d762c323c01762cc6f25c0001', '/images', 'Add_Edit_Multilingual_Data_Step_1.PNG', 'd6e30474-ca6c-4b3c-8894-090f1d985de5', 'admin@jquiver.io', NOW(), 'helpManual', '9c25fb63-8336-4f22-bb97-a5042159d5c4'), 
('40289d3d762c323c01762cc7015f0002', '/images', 'Add_Edit_Multilingual_Data_Step_2.PNG', '3d331031-fb3c-4aaa-81f9-e18dc99d2812', 'admin@jquiver.io', NOW(), 'helpManual', '9c25fb63-8336-4f22-bb97-a5042159d5c4'), 
('40289d3d762c323c01762cc712310003', '/images', 'Multilingual_Demo.PNG', '5a1a9bf9-ce85-483c-8eb9-a9ce0d8884b9', 'admin@jquiver.io',NOW(), 'helpManual', '9c25fb63-8336-4f22-bb97-a5042159d5c4'), 
('40289d3d762c323c01762cc71ee00004', '/images', 'Multilingual_Demo_French.PNG', '688edc01-ec45-4a09-a08e-d18f7b5000ee', 'admin@jquiver.io', NOW(), 'helpManual', '9c25fb63-8336-4f22-bb97-a5042159d5c4'), 
('40289d3d762c323c01762cc72f480005', '/images', 'Multilingual_Demo_Hindi.PNG', '86247f52-2f06-4506-b8e5-03056d3bbaca', 'admin@jquiver.io', NOW(), 'helpManual', '9c25fb63-8336-4f22-bb97-a5042159d5c4'),
('40289d3d763887410176388995320000', '/images', 'Google_Analytics_Application_Configuration.PNG', '2e4403fd-9d8f-42ed-a782-551e45f50a59', 'admin@jquiver.io', NOW(), 'helpManual', '918676c8-b653-43ee-964a-d4faaeb13787'), 
('40289d3d763887410176388b36b60001', '/images', 'Dev_Environment_Application_Configuration.PNG', '11fdbaad-55f3-48e6-8032-4fd650df0e30', 'admin@jquiver.io', NOW(), 'helpManual', '918676c8-b653-43ee-964a-d4faaeb13787'),
('4028168b7647e50a0176488ec8860003', '/images', 'Template_Without_Param.PNG', 'c9336bc5-f083-42cd-bf70-152ef859b638', 'admin@jquiver.io', NOW(), 'helpManual', '17feffba-99f4-4591-9cb5-0fef46ee0b77'), 
('4028168b7647e50a0176488f32110004', '/images', 'Template_With_Param.PNG', '3fb18376-b4df-43c7-bfde-a62ac98e241b', 'admin@jquiver.io', NOW(), 'helpManual', '17feffba-99f4-4591-9cb5-0fef46ee0b77'), 
('4028168b7647e50a0176488f4ebe0005', '/images', 'Resource_Bundle_Without_Default.PNG', 'db085e59-de62-440c-a8c5-039595a8392a', 'admin@jquiver.io', NOW(), 'helpManual', '17feffba-99f4-4591-9cb5-0fef46ee0b77'), 
('4028168b7647e50a0176488f60ba0006', '/images', 'Resource_Bundle_With_Default.PNG', '79fd4660-5fd7-40c0-9238-92a0b9e91cae', 'admin@jquiver.io',NOW(), 'helpManual', '17feffba-99f4-4591-9cb5-0fef46ee0b77'),

('4028168b7647e50a017648c9a2fc0008', '/images', 'Profile_Property.PNG', '284056ff-151a-4230-a044-cf04306b89bf', 'admin@jquiver.io', NOW(), 'helpManual', 'helpManual'), 

('4028168b7647e50a017648ca0c690009', '/images', 'Template_Storage_Location_Property.PNG', '425284ef-03b1-4fbd-81f2-6649c6fb0404', 'admin@jquiver.io', NOW(), 'helpManual', '935b9394-c33d-4113-a248-27c46c45e7e9'),
('40289d3d765d4f9c01765d67c7d90005', '/images', 'Template_Download_All.PNG', '118e8bd6-1fa5-4b72-b30a-64e4ac09a447', 'admin@jquiver.io', NOW(), 'helpManual', '935b9394-c33d-4113-a248-27c46c45e7e9'), 
('40289d3d765d4f9c01765d67d7850006', '/images', 'Template_Upload_All.PNG', '27d6eba0-55b4-4e82-82fb-4c16d38d1d19', 'admin@jquiver.io', NOW(), 'helpManual', '935b9394-c33d-4113-a248-27c46c45e7e9'), 
('40289d3d765d4f9c01765d67e8320007', '/images', 'Dynamic_Form_Download_All.PNG', '2684d1d6-6650-4d08-bed8-fb229afa969c', 'admin@jquiver.io', NOW(), 'helpManual', '935b9394-c33d-4113-a248-27c46c45e7e9'), 
('40289d3d765d4f9c01765d67f5d90008', '/images', 'Dynamic_Form_Upload_All.PNG', '58bd210e-47d5-4516-a9dc-89b27d32d49d', 'admin@jquiver.io', NOW(), 'helpManual', '935b9394-c33d-4113-a248-27c46c45e7e9'), 
('40289d3d765d4f9c01765d7279a50009', '/images', 'Dashlet_Download_All.PNG', '8090ddee-8014-46a0-b147-6c2ed5fd6d57', 'admin@jquiver.io', NOW(), 'helpManual', '935b9394-c33d-4113-a248-27c46c45e7e9'), 
('40289d3d765d4f9c01765d7287ee000a', '/images', 'Dashlet_Upload_All.PNG', '092a5276-c6c6-4710-a50b-c1913b8c2d36', 'admin@jquiver.io', NOW(), 'helpManual', '935b9394-c33d-4113-a248-27c46c45e7e9'),
('40289d3d7660efd0017660f91702000a', '/images', 'Template_Action_Buttons.PNG', '65b32265-e86e-4e87-9901-2e986dda2dc1', 'admin@jquiver.io', NOW(), 'helpManual', '935b9394-c33d-4113-a248-27c46c45e7e9'), 
('40289d3d7660efd0017660f9a518000b', '/images', 'Dashlet_Action_Buttons.PNG', '4a848d47-474d-4d27-a597-adf003fd094a', 'admin@jquiver.io', NOW(), 'helpManual', '935b9394-c33d-4113-a248-27c46c45e7e9'), 
('40289d3d7660efd0017660f9ba53000c', '/images', 'Dynamic_Form_Action_Buttons.PNG', '46d81eed-e1a6-4db2-b02f-bb617321f08f', 'admin@jquiver.io', NOW(), 'helpManual', '935b9394-c33d-4113-a248-27c46c45e7e9'),
('40289d3d7661398f0176615d9dbd0001', '/images', 'Template_Folder_Structure.PNG', 'fa1b8ef9-5a89-49b1-935d-25959e47ed21', 'admin@jquiver.io', NOW(), 'helpManual', '935b9394-c33d-4113-a248-27c46c45e7e9'),
('402816927661a061017661a88a860006', '/images', 'Dashlet_Folder_Structure_1.PNG', '2b9debb5-9f13-40e6-9d58-e51a7c83e0d7', 'admin@jquiver.io', NOW(), 'helpManual', '935b9394-c33d-4113-a248-27c46c45e7e9'),
('40289d3d7661398f0176617e3aee0004', '/images', 'Dashlet_Folder_Structure_2.PNG', '850316b0-bd29-41a3-88b8-30764903b73b', 'admin@jquiver.io',  NOW(), 'helpManual', '935b9394-c33d-4113-a248-27c46c45e7e9'),
('402816927661a061017661a86f8c0004', '/images', 'Dynamic_Form_Folder_Structure_1.PNG', 'a0661c6d-887b-44eb-9bb4-ac29cfe78dc3', 'admin@jquiver.io', NOW(), 'helpManual', '935b9394-c33d-4113-a248-27c46c45e7e9'),
('40289d3d7661398f0176617633730002', '/images', 'Dynamic_Form_Folder_Structure_2.PNG', 'b7568e9a-c31b-4492-bea1-bfe580a580ec', 'admin@jquiver.io',  NOW(), 'helpManual', '935b9394-c33d-4113-a248-27c46c45e7e9'),
('40289d3d765d4f9c01765d5514740000', '/images', 'Profile_Property.PNG', 'e8ba205c-1f99-46e6-891f-cf7f220daf76', 'admin@jquiver.io', NOW(), 'helpManual', '935b9394-c33d-4113-a248-27c46c45e7e9'),

('4028b8817646ed03017647dc3ab40021', '/images', 'manageRole.PNG', '1eab0154-70c2-4b19-824b-3a99a7f72bb5', 'admin@jquiver.io', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'), 
('4028b8817646ed03017647ef2f050022', '/images', 'manageUser.PNG', '55072ccf-76cf-4a20-b66b-8d9ba9371e38', 'admin@jquiver.io', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'), 
('4028b8817646ed03017647f1c1330023', '/images', 'forcePasswordMail.PNG', '076e8dd4-6389-4a13-be07-c524e96dbef7', 'admin@jquiver.io', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'), 
('4028b8817646ed030176480a3c080024', '/images', 'manageRoleModule.PNG', '50b32b99-29e1-45e4-8920-84701895e783', 'admin@jquiver.io', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'), 
('4028b8817646ed030176480ce3fd0025', '/images', 'manageEntityRole.PNG', '891bd5e4-043a-41eb-a623-95856f23c453', 'admin@jquiver.io', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'), 
('4028b8817646ed030176480e43ab0026', '/images', 'userManagement.PNG', '75354d31-d32b-41d8-877c-f15b86bac8da', 'admin@jquiver.io', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('4028b8817650f1220176511211c40002', '/images', 'authTypes.png', '85b8a915-bd77-4b54-9e39-0fcbc27fb9d0', 'admin@jquiver.io', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'), 
('4028b8817650f12201765116077e0003', '/images', 'databaseAuth-1.png', 'ebfd663a-42ec-4ce9-9190-f0f9b6acba74', 'admin@jquiver.io', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'), 
('4028b8817650f122017651166e0d0004', '/images', 'databaseAuth-2.png', '9563f6a8-7ef5-4bef-8db5-2a89edf07c75', 'admin@jquiver.io', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'), 
('4028b8817650f12201765121c1300005', '/images', 'databaseAuth-password.png', '7f733b86-f55b-4eb5-ab5d-1a0f530aeeec', 'admin@jquiver.io', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('4028b8817650f122017651226ab90006', '/images', 'databaseAuth-password-captcha.png', '43966179-960e-4339-aa47-94d676a44aa0', 'admin@jquiver.io', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'), 
('4028b8817650f12201765122f1750007', '/images', 'databaseAuth-totp.png', '93c8b0c5-3e68-444b-8c64-fb86c6aaf62f', 'admin@jquiver.io',NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('4028b881765fd657017660116dac0000', '/images', 'oauth-clients.png', '394f9c18-0b9e-4f05-ac14-ca4a754fc018', 'admin@jquiver.io', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('4028b881765fd65701766028c6f60001', '/images', 'google-credentials.PNG', 'e6599f51-6988-4ec3-b382-a0caeb78105a', 'admin@jquiver.io', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('4028b881765fd6570176602a9c080002', '/images', 'google-create-project.PNG', '3df83556-44da-48a1-a68b-7ad6d2d84b14', 'admin@jquiver.io', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('4028b881765fd6570176602ca2d50003', '/images', 'google-configure-consent-screen.PNG', '554929bc-c01a-4f7b-8786-386107c7e682', 'admin@jquiver.io',NOW() , 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('4028b881765fd6570176602d10ac0004', '/images', 'google-configure-consent-screen-2.PNG', '86102523-6b13-4313-8d27-c9db1816d54f', 'admin@jquiver.io', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'), 
('4028b881765fd6570176609447520005', '/images', 'google-credentials-3.PNG', '67d1978e-1274-4e51-9a75-4ffbba12a8d3', 'admin@jquiver.io', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'), 
('4028b881765fd657017660d3c0550006', '/images', 'facebook-1.PNG', 'e0820928-668d-4558-bd8c-e4e9d190a764', 'admin@jquiver.io', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('4028b881765fd657017660d441bd0007', '/images', 'facebook-2.PNG', 'a9a61d99-4697-4482-b791-b651fa49373e', 'admin@jquiver.io', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'), 
('4028b881765fd657017660d70c6d0008', '/images', 'offcie365-1.PNG', '8f1ca6b2-b9e5-4dfa-ac9b-1976637abc4b', 'admin@jquiver.io', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'), 
('4028b881765fd657017660d8b7380009', '/images', 'offcie365-2.PNG', 'd1ef583f-6fc7-4a58-a5d8-9e015052f0f8', 'admin@jquiver.io', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'), 
('4028b881765fd657017660d92df9000a', '/images', 'offcie365-3.PNG', 'cad0118f-f44f-48fa-b6de-ff2b6fc74fa2', 'admin@jquiver.io', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'), 
('4028b881765fd657017660da0caa000b', '/images', 'offcie365-4.PNG', '4e4a9bd9-d0ed-44e0-9b14-263bcc39f25c', 'admin@jquiver.io', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'), 
('4028b881765fd657017660da9464000c', '/images', 'offcie365-5.PNG', 'bf29893b-b207-4982-b350-a32c8ca377a0', 'admin@jquiver.io', NOW() , 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('40289d8f76a600980176a60bfa870002', '/images', 'User_Details_In_JS.png', 'fe9a16c1-bf80-4269-92a3-83bc33b48864', 'admin@jquiver.io', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),

('40289d3d765124480176512a5e0f0000', '/images', 'Autocomplete_Example_1.PNG', 'cb9f36dd-18be-429a-b6be-9a6042f06c9a', 'admin@jquiver.io', NOW(), 'helpManual', '5e46df00-e07a-4b73-889f-2894adfd3df8'), 
('40289d3d765124480176512a6e660001', '/images', 'Autocomplete_Example_2.PNG', 'f77ad3ca-d27c-4a0a-a2e7-e9a4ebd776a0', 'admin@jquiver.io', NOW(), 'helpManual', '5e46df00-e07a-4b73-889f-2894adfd3df8'), 
('40289d3d765124480176512a79560002', '/images', 'Autocomplete_Example_3.PNG', '17fb0677-f999-4fe0-8cd9-7190098a7e8d', 'admin@jquiver.io', NOW(), 'helpManual', '5e46df00-e07a-4b73-889f-2894adfd3df8'),
('40289d3d765124480176512a89830003', '/images', 'Autocomplete_Example_4.PNG', 'bf5e5d91-56f2-4c75-89aa-458612aba3cd', 'admin@jquiver.io', NOW(), 'helpManual', '5e46df00-e07a-4b73-889f-2894adfd3df8'), 
('40289d3d765124480176512a96630004', '/images', 'MultiSelect_Example_1.PNG', '99a536d6-7dd4-4e7e-9702-6a312c14cc8a', 'admin@jquiver.io', NOW(), 'helpManual', '5e46df00-e07a-4b73-889f-2894adfd3df8'), 
('40289d3d765124480176512aa3f80005', '/images', 'MultiSelect_Example_2.PNG', 'db6394b8-d7a6-4231-be9a-f587794e2f99', 'admin@jquiver.io', NOW(), 'helpManual', '5e46df00-e07a-4b73-889f-2894adfd3df8'),


('40288089766a9a9501766acfa31b0002', '/images', 'export_config.PNG', '921e106e-a5d5-464e-a2f3-75902adeb49e', 'admin@jquiver.io', NOW(), 'helpManual', 'dd97c23d-feef-4cea-afcf-3cece7819159'),
('40288089766a9a9501766acfe7920003', '/images', 'preview_export.PNG', '8cb04e93-41c1-4d34-9e4a-5d0eb5b9c461', 'admin@jquiver.io', NOW(), 'helpManual', 'dd97c23d-feef-4cea-afcf-3cece7819159'),
('40288089766a9a9501766ad0ba300004', '/images', 'export_menu_db.PNG', '2278931c-ce43-4bb8-86dc-6b202b63b1a0', 'admin@jquiver.io', NOW(), 'helpManual', 'dd97c23d-feef-4cea-afcf-3cece7819159'),

('40288089766b4ac701766b5f263d0017', '/images', 'import_config.PNG', '12fb5a7f-ac0e-4dfc-b14d-a65310d75e1e', 'admin@jquiver.io', NOW(), 'helpManual', 'be37c240-2607-4d79-9ef1-136dbd7c524b'),
('40288089766b4ac701766b61fa4a0019', '/images', 'import_compare.PNG', '0945e9ce-b63d-4505-852d-491e56b4f74b', 'admin@jquiver.io', NOW(), 'helpManual', 'be37c240-2607-4d79-9ef1-136dbd7c524b'),

('40289d3d768650810176866b317e0003', '/images', 'File_Upload_Manager_Master.PNG', '3c60e10d-c0cd-4b60-944f-b9a54e9faa45', 'admin@jquiver.io', NOW(), 'helpManual', '6938f0ac-00fe-4b94-95e7-02ef72016fe4'), 
('40289d3d768650810176866b46ad0004', '/images', 'Add_File_Configuration_Step_1.PNG', 'dab82912-46c3-46e0-9d4e-d2b4abb1e283', 'admin@jquiver.io', NOW(), 'helpManual', '6938f0ac-00fe-4b94-95e7-02ef72016fe4'), 
('40289d3d768650810176866b52100005', '/images', 'Add_File_Configuration_Step_2.PNG', '750c9d79-1970-48da-8f8d-5ebcde096944', 'admin@jquiver.io', NOW(), 'helpManual', '6938f0ac-00fe-4b94-95e7-02ef72016fe4'),
('4028b88176a8f81a0176a9aad1090000', '/images', 'form-builder-welcome.png', '99300f8f-0ad6-4021-8bcd-f679a6f32b8c', 'admin@jquiver.io',NOW() , 'helpManual', '6938f0ac-00fe-4b94-95e7-02ef72016fe4'),
('4028b88176a8f81a0176a9ae0cf80001', '/images', 'form-builder-listing.PNG', 'eb941ec9-1eb2-47c4-a0cc-0ce53145a540', 'admin@jquiver.io',NOW(), 'helpManual', '6938f0ac-00fe-4b94-95e7-02ef72016fe4'),
('4028b88176ae0bd20176ae160c010000', '/images', 'form-builder-template.png', '2c5ac0a7-47fe-480b-b9c7-7982a2ec99a6', 'admin@jquiver.io', NOW(), 'helpManual', '6938f0ac-00fe-4b94-95e7-02ef72016fe4'), 
('4028b88176ae0bd20176ae1843480001', '/images', 'form-builder-populate.png', 'b6307aae-374f-499f-87f6-82add750c0e6', 'admin@jquiver.io', NOW(), 'helpManual', '6938f0ac-00fe-4b94-95e7-02ef72016fe4'), 
('4028b88176ae0bd20176ae1abd670002', '/images', 'form-builder-populate-select.png', '7a17a93c-4365-42c2-ab2f-b7a0e65254ac', 'admin@jquiver.io', NOW(), 'helpManual', '6938f0ac-00fe-4b94-95e7-02ef72016fe4'), 
('4028b88176ae0bd20176ae1bbc760003', '/images', 'form-builder-populate-save.png', '134dccf1-2974-48fc-aa51-021f096f6724', 'admin@jquiver.io', NOW(), 'helpManual', '6938f0ac-00fe-4b94-95e7-02ef72016fe4'),
('5d45f43e-5ba0-471a-8fa5-80de72d12eb0', '/images', 'mail_config.png', '5712df30-f66b-4569-a0ee-04e075fe1d41', 'admin@jquiver.io', NOW(), 'helpManual', '918676c8-b653-43ee-964a-d4faaeb13787'),

('0f684eec-041c-4e6e-8e69-025e487d3356', '/images', 'help_manual_1.png', 'f45512c5-4fe6-4741-abc6-933cb825630c', 'admin@jquiver.io', NOW(), 'helpManual', '4dddd5a0-d69f-4e6c-bd91-0fa10e8adbfb'),
('5831d7d7-d16c-4048-9bf1-d2ee1fdc127c', '/images', 'help_manual_2.png', '53407a0b-4672-43fc-be89-5e2503a9e2e9', 'admin@jquiver.io', NOW(), 'helpManual', '4dddd5a0-d69f-4e6c-bd91-0fa10e8adbfb'),
('b71e324a-4cd8-48e9-9740-fd792e9c7810', '/images', 'help_manual_3.png', '664d3641-2a28-461f-8146-3be79db10271', 'admin@jquiver.io', NOW(), 'helpManual', '4dddd5a0-d69f-4e6c-bd91-0fa10e8adbfb'),
('c8311426-74b8-4811-9319-37a04c47c598', '/images', 'help_manual_4.png', '655509c1-9244-4a43-8ed1-9213ad621630', 'admin@jquiver.io', NOW(), 'helpManual', '4dddd5a0-d69f-4e6c-bd91-0fa10e8adbfb');


REPLACE INTO jq_file_upload (file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id) VALUES
('1f178e13-be12-4c29-bc47-9910a867af6c', 'helpManual', '/images', 'Site_Layout_Add_Step_1.PNG', '433b8358-886f-4235-bfb2-4d11c1c58256', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),  
('dc8c094d-700b-40a9-8d7c-49e1aa55e1dd', 'helpManual', '/images', 'Site_Layout_Add_Step_2.PNG', '7d57f226-dfc9-4339-8837-c5236e351f9b', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('00b01a2d-bdeb-40aa-b457-169988480295', 'helpManual', '/images', 'Site_Layout_Add_Step_3.PNG', '2475102b-eee9-43a7-b4b9-463bbe829531', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e');


REPLACE INTO jq_file_upload (file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id) VALUES
('09f4260c-0cef-4569-b806-7589ee2a108f', 'helpManual', '/images', 'Dashboard_Inside_Menu_Step_1.png', 'b6580410-a2b2-4831-9315-e8165298fd08', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('6011972c-0544-4322-8bb8-6229463682cb', 'helpManual', '/images', 'Dashboard_Inside_Menu_Step_2.png', '251cfb51-99f8-432d-8cfa-cc069f498636', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('c288ea4f-e848-425a-9e1a-c0284d447079', 'helpManual', '/images', 'Dashboard_Inside_Menu_Step_3.png', '6a8a351a-4f86-4a81-896e-728b84aa800f', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('5d4065f3-78d7-4480-be77-ac3f35dff0ff', 'helpManual', '/images', 'Dashboard_Inside_Menu_Step_4.png', 'bc2ba223-cce8-43e0-8cbf-af53e9b45752', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('b0c436b2-4c76-49f7-b960-a537c09e7d21', 'helpManual', '/images', 'Dashboard_Inside_Menu_Step_5.png', '2ab7a19c-5c34-4b02-af57-daa168ba156f', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e');
 
 
REPLACE INTO jq_file_upload(file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id) VALUES
('6f796eb0-bafa-4dcd-9c8a-d2469be048fe', 'helpManual', '/images', 'Dynamic_Form_Inside_Menu_Step_4.png', 'f09580dc-6235-4cbf-ad3f-12f1d405ed29', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('9f441835-28b8-425d-b21f-abbced54dfa2', 'helpManual', '/images', 'Dynamic_Form_Inside_Menu_Step_2.png', '8146c655-4805-4812-9f33-7690216996f2', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('c4fd6ce2-5071-4962-bc8f-56bfce3b2907', 'helpManual', '/images', 'Dynamic_Form_Inside_Menu_Step_1.png', 'fd6a3e46-8c0e-447a-9011-8426d801158a', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('da04a076-ed62-4f42-b1e8-91372a3e0fc4', 'helpManual', '/images', 'Dynamic_Form_Inside_Menu_Step_3.png', 'ed0ee55f-01e6-4c7d-b57d-1363e9020a4f', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('f24d2e80-8b38-48a0-bfac-5a2488736086', 'helpManual', '/images', 'Dynamic_Form_Inside_Menu_Step_5.png', '13a170c6-9fa1-482e-80a0-a8bec500a084', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e');


REPLACE INTO jq_file_upload(file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id) VALUES
('3860998e-33ab-4dcb-a27d-0cce0aa73337', 'helpManual', '/images', 'REST_API_Inside_Menu_Step_5.PNG', '8f96d948-c4cd-4038-a5a3-b8861d4d932c', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('59ad8aa2-fcbe-4ff8-85b4-b92fb09d2b81', 'helpManual', '/images', 'REST_API_Inside_Menu_Step_2.PNG', '41b34f24-3523-48a7-bace-d814aff8f60b', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('717c32c4-be30-47d7-b55e-25e11b20b195', 'helpManual', '/images', 'REST_API_Inside_Menu_Step_1.png', 'd5756ba3-a3b7-4745-875c-feb60e0c8690', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('a3f2c013-dd80-4cd0-957f-a3ae8da725e1', 'helpManual', '/images', 'REST_API_Inside_Menu_Step_4.PNG', '942e34bb-763c-4425-8b53-65ae682616ca', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('fc522253-f73a-4980-a43b-c096d82e6081', 'helpManual', '/images', 'REST_API_Inside_Menu_Step_3.PNG', '3994b593-c055-4f8c-9a2e-5620a0ca71bb', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'); 


REPLACE INTO jq_file_upload(file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id) VALUES
('2942ed27-f194-459a-8121-0106b9d3d9a4', 'helpManual', '/images', 'Template_Inside_Menu_Step_1.png', '63027d6b-0c5a-4d0a-ba06-389028759f6d', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('72d8474a-7d4d-4aa8-8528-833679baf9be', 'helpManual', '/images', 'Template_Inside_Menu_Step_2.png', '40cdc11c-2c5f-4852-aa2b-92f9100176e1', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('a3f4f471-0386-40ee-b21b-e91841083822', 'helpManual', '/images', 'Template_Inside_Menu_Step_3.png', '025fdd3a-2404-4cfb-a5c0-c39a000ba0d4', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('649fab90-76a9-4135-b271-71b9388140a7', 'helpManual', '/images', 'Template_Inside_Menu_Step_4.png', '33190738-3180-4c41-859c-cf01597d25a2', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('a3e471fd-f9be-4987-bb4f-9db93a9cbb06', 'helpManual', '/images', 'Template_Inside_Menu_Step_5.png', 'b5e0a006-0309-4b59-a73a-6df82b71a734', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e');


REPLACE INTO jq_file_upload(file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id) VALUES
('577dfda4-41cb-4c8a-ad9d-bdb769140d36', 'helpManual', '/images', 'Role_Based_Home_Page_Step_1.png', 'fc8495bc-1184-4e5b-ad32-949ec461f24a', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('44c3eaa9-82a7-4c66-8ba5-af20ab5ff087', 'helpManual', '/images', 'Role_Based_Home_Page_Step_10.png', '24dab333-fc53-40f5-8983-6930858a0a5b', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('ecb167a8-bb55-4c23-be00-32b88e8903d1', 'helpManual', '/images', 'Role_Based_Home_Page_Step_11.png', 'ad604204-33ff-4d55-930e-7a9b8211d8d4', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('56ebcae1-14b7-4855-b5d5-9e936c961347', 'helpManual', '/images', 'Role_Based_Home_Page_Step_12.png', '03690127-cea1-4e3c-8202-b865447e8f00', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('f207f7fb-0a09-471d-b17d-93f1b55f0f86', 'helpManual', '/images', 'Role_Based_Home_Page_Step_2.png', 'aca811d4-d0e9-4124-aa87-567c8608467a', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('ac0a0f40-cbc8-4f52-8125-a2d7808a2ff4', 'helpManual', '/images', 'Role_Based_Home_Page_Step_3.png', 'f89270d9-2c2d-4ecc-82bd-467ab2fa952c', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('0a7a4f60-8819-43fd-aa85-90a02bb042ba', 'helpManual', '/images', 'Role_Based_Home_Page_Step_4.png', '2cc37aa0-e941-48ab-a174-7967cb95ce82', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('4daeff56-7cbc-4f9b-ba29-3c60021ec634', 'helpManual', '/images', 'Role_Based_Home_Page_Step_5.png', 'b329c929-edb7-41e9-905a-49c0e409df14', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('29f15fe9-bd19-4f4a-a4c9-ec198a9b078b', 'helpManual', '/images', 'Role_Based_Home_Page_Step_6.png', '29f61697-ac85-49f6-90c7-3b4aeb3595b9', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('9656a918-91c6-4352-8dbb-07860e404e53', 'helpManual', '/images', 'Role_Based_Home_Page_Step_7.png', '376a790c-076e-49b5-a912-d0ce63239e15', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('ecbb9c05-228b-4a0d-b65c-202891905318', 'helpManual', '/images', 'Role_Based_Home_Page_Step_8.png', 'a41d3d1a-ff19-45b0-ac86-3f3a6dbc5870', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e');

DELETE FROM jq_file_upload WHERE file_upload_id = 'fc7c2308-b17f-41b8-befc-64a383f45572';

REPLACE INTO jq_file_upload(file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id) VALUES
('93d0e859-4726-41ea-af6d-743258cbecd1', 'helpManual', '/images', 'Site_Layout_Group_Step_1.PNG', '82d226ad-56b6-4d48-9cbc-c08c393bff10', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('758ba2cd-ff8a-4865-94b2-4d062ffab93c', 'helpManual', '/images', 'Site_Layout_Group_Step_2.PNG', '625cbc3a-164e-41f3-a4e8-e7bcc8709712', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('553b2332-bf8b-462b-9c5c-d89bc0682a92', 'helpManual', '/images', 'Site_Layout_Group_Step_3.PNG', '25b51eba-91d4-4ac7-bbd7-2de0e4f6881f', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('b3fd7730-a501-4a50-afed-54a956151748', 'helpManual', '/images', 'Site_Layout_Group_Step_4.PNG', '4a7c9f70-5626-4e7d-8098-f6b5f6a18113', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e');


REPLACE INTO jq_file_upload(file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id) VALUES
('b92c317e-d2fd-44b5-a972-9ca18af4705d', 'helpManual', '/images', 'Site_Layout_Path_Variable_Step_1.PNG', '7b74f2b0-4a42-40a2-9730-b6404055e698', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('8aa4e0ca-f775-404e-972f-396bf639390e', 'helpManual', '/images', 'Site_Layout_Path_Variable_Step_2.PNG', '37e52e84-f59e-44c1-9dfc-e9ef80e7dfd6', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('c41a3346-5a70-4c56-b015-76f527c389cd', 'helpManual', '/images', 'Site_Layout_Path_Variable_Step_3.PNG', 'd809736c-085f-4188-9312-6608a47f8bd7', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e');


REPLACE INTO jq_file_upload(file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id) VALUES
('5ea356bb-88d3-4097-b44d-d96ec273ed2f', 'helpManual', '/images', 'Master_Generator_1.png', '3fad5943-291d-4677-bff7-a3d1972fadd3', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'), 
('6cdc83c4-e2b2-4eb8-b9cb-17aff7c51a58', 'helpManual', '/images', 'Master_Generator_2.png', '398a7815-1709-48fc-a19c-c1c44072aa6f', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'), 
('8b512d2c-6f59-433b-a6b4-b1bdd22479b3', 'helpManual', '/images', 'Master_Generator_3.png', 'd18dbccb-eddc-4407-ac07-ab707d632ec7', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'), 
('8d1522cb-a12a-474e-a870-2d63098b620b', 'helpManual', '/images', 'Master_Generator_4.png', 'f5b9927b-7f12-426f-ba65-9932a1a0b7b3', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'), 
('97df2f91-924d-4d86-b999-72f43bf4c2bc', 'helpManual', '/images', 'Master_Generator_5.png', '6a9a57d9-9fde-4077-a59b-8a4c5c2cfb07', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('9e9fcdb7-f54d-4c04-aee5-76a35a0559d8', 'helpManual', '/images', 'Master_Generator_6.png', 'e4a6b905-5d1d-41a3-b409-a1b5c5f8d967', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('d560cd3c-82f8-4c7b-927b-77c679cdcf51', 'helpManual', '/images', 'Master_Generator_7.png', 'b995d553-986f-460b-b204-b25614a71be5', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('c3779826-2f8b-42d1-afeb-8302482f468f', 'helpManual', '/images', 'Master_Generator_8.png', 'dfe0bb67-d499-45e8-b0e2-6899ddca5d27', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('66b7db46-3bbf-40fc-b0aa-24ae8e3febba', 'helpManual', '/images', 'Master_Generator_9.png', '59d7833b-1980-4917-a30c-f3211c8d7a3c', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'), 
('68d76985-5616-4fb4-a481-b25fd6ccf888', 'helpManual', '/images', 'Master_Generator_10.png', '5de7a2a8-5cfb-45c8-8cc6-f1776ffbddfe', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'), 
('88260a66-eaaf-40fb-b383-518e131cb6f9', 'helpManual', '/images', 'Master_Generator_11.png', '8ddbe404-ea02-46d3-8e88-034819ff6990', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'), 
('a3f74af1-8159-4134-942f-5d4655d4203e', 'helpManual', '/images', 'Master_Generator_12.png', 'b76177d8-1476-4105-817d-45fdf1eb32e6', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'), 
('a501ef3d-bcc9-4d42-8990-99ddeb97fbe4', 'helpManual', '/images', 'Master_Generator_13.png', '2696c9d5-f829-425b-a6ea-af5b56f22e88', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('a6b9c378-6ffc-4874-870e-271d829103cf', 'helpManual', '/images', 'Master_Generator_14.png', '6a1c51fc-1c1c-4c94-853e-ffd9633bc1b3', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('b0313ee9-32b5-42bc-8b8c-9ad79c942c42', 'helpManual', '/images', 'Master_Generator_15.png', '2ed613e3-5e90-4071-954f-36b726219da9', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('e8534c20-a6cf-456a-82bc-8290c4a14792', 'helpManual', '/images', 'Master_Generator_16.png', 'c73f141b-f96c-430c-8429-a70cdd57d7c6', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('f625d2c2-6073-46a3-adc0-64d903aeaaa7', 'helpManual', '/images', 'Master_Generator_17.png', '2ae0daec-3ade-455f-919e-2ae7ef44cf5c', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46');



REPLACE INTO jq_file_upload(file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id) VALUES
('1bd91c6b-3f30-4481-8803-f9a8fe8b8626', 'helpManual', '/images', 'Project_Name_Change_Password.PNG', '91bea704-9087-43d9-94d2-8bffce12940a', 'admin@jquiver.io', NOW(), '17feffba-99f4-4591-9cb5-0fef46ee0b77'), 
('4ab78613-7f7b-46b5-af26-73a3643aaea0', 'helpManual', '/images', 'Project_Name_Home_Page.PNG', 'f51ffbc2-6de8-4fe7-91ef-616834458d33', 'admin@jquiver.io', NOW(), '17feffba-99f4-4591-9cb5-0fef46ee0b77'), 
('8b01d2d4-1bd8-4a54-a460-f50202cd0471', 'helpManual', '/images', 'Project_Name_Login_Page.PNG', '1815a60e-4ad0-4f96-9e82-8e638c692c16', 'admin@jquiver.io', NOW(), '17feffba-99f4-4591-9cb5-0fef46ee0b77'), 
('8261a46a-3ece-4ff6-a81e-742ac55d1cfa', 'helpManual', '/images', 'Project_Name_Reset_Password.PNG', '82360982-7fca-4ebe-9ada-8f5b12dfce14', 'admin@jquiver.io', NOW(), '17feffba-99f4-4591-9cb5-0fef46ee0b77'), 
('72ad938d-a6de-4d16-9203-27cc8b12f649', 'helpManual', '/images', 'Project_Name_Reset_Password_TOTP.PNG', '3ca18b25-e32e-40ff-bea9-667bd4ec8b39', 'admin@jquiver.io', NOW(), '17feffba-99f4-4591-9cb5-0fef46ee0b77'), 
('1fca4824-251f-4426-abdc-8cff655fbfa4', 'helpManual', '/images', 'Project_Name_Resource_Key_1.PNG', 'ce6e2730-3d8a-4684-869c-55907fc16601', 'admin@jquiver.io', NOW(), '17feffba-99f4-4591-9cb5-0fef46ee0b77'), 
('3a1e89b9-f0cb-42aa-ab7d-041e08daf536', 'helpManual', '/images', 'Project_Name_Resource_Key_2.PNG', '173c0ba8-52c4-43e6-90cd-b55f71bdf8bb', 'admin@jquiver.io', NOW(), '17feffba-99f4-4591-9cb5-0fef46ee0b77');

REPLACE INTO jq_file_upload(file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id) VALUES
('03f110de-811d-47f3-be2e-fc6196c11ace', 'helpManual', '/images', 'File_Bin_1.PNG', '13ff9b2e-1410-4346-b893-8d82f4e39e69', 'admin@jquiver.io', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'), 
('05777b2b-c424-4472-8678-363d4fe3ac5d', 'helpManual', '/images', 'File_Bin_2.PNG', 'c020f88f-af4f-4b1e-a46b-63663722cd97', 'admin@jquiver.io', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'), 
('62765d11-c5ad-4128-a94a-f55ac5b36bbe', 'helpManual', '/images', 'File_Bin_3.PNG', 'f922428f-9fee-482d-857b-84804ca99e28', 'admin@jquiver.io', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'), 
('6908081e-3441-40f0-87db-3e55f00feb83', 'helpManual', '/images', 'File_Bin_4.PNG', '36d1314c-1545-4010-a1af-4f5ad77470d9', 'admin@jquiver.io', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'), 
('6d2b0993-be4c-43c8-a72f-19eaf5a6ee93', 'helpManual', '/images', 'File_Bin_5.PNG', '76e5284c-5b03-4aa3-8775-a954067455dd', 'admin@jquiver.io', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'), 
('74909e25-3efa-41c7-a11f-8e02dcc1768c', 'helpManual', '/images', 'File_Bin_6.PNG', '0f1f2aef-7a8f-4c53-a214-6b4c7e1412c0', 'admin@jquiver.io', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'), 
('8606b360-59e0-469f-86ea-737605981c04', 'helpManual', '/images', 'File_Bin_7.PNG', '8f54cc6c-bc40-4915-90d3-d5cc4bb8c61d', 'admin@jquiver.io', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'),
('b71984b5-841b-4b6e-bffa-77ca956105dc', 'helpManual', '/images', 'File_Bin_8.PNG', '8a7082db-8725-4b52-bb18-72e9695c9c07', 'admin@jquiver.io', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'), 
('c57e84f0-aa52-4597-98d8-9eb2afd90cc5', 'helpManual', '/images', 'File_Bin_9.PNG', '84c266c3-57c7-4974-82dd-06e982c7d9ff', 'admin@jquiver.io', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'), 
('8c901fa8-4c71-48dd-bd1c-f2e6bdba2fce', 'helpManual', '/images', 'File_Bin_11.PNG', '677b6e6f-83fc-4fbb-8641-ad0f822fad98', 'admin@jquiver.io', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'), 
('a318aa76-ce66-446e-8418-fdbc62ede7c8', 'helpManual', '/images', 'File_Bin_12.PNG', '9b959e22-2ac8-4321-ab90-f1fa4d7a6de5', 'admin@jquiver.io', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'), 
('f288f186-d985-4a53-abb1-4b293ef92bf1', 'helpManual', '/images', 'File_Bin_13.PNG', '03daf48e-e9b8-4440-9e74-2610ee6bd628', 'admin@jquiver.io', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538');


REPLACE INTO jq_file_upload(file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id) VALUES
('6079c4c4-167a-4a16-bdee-56c9f93f986b', 'helpManual', '/images', 'REST_API_FTL_Example.PNG', '4d214e32-4b3f-4621-b454-b5ce61878cce', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'), 
('c6b5c730-804f-4aab-9a22-ea8d2b32ad63', 'helpManual', '/images', 'REST_API_FTL_STEP_1.PNG', 'dbf619bb-353f-4745-a695-ffcf988bbff4', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'), 
('29792cef-d7e7-43ba-a15a-36ec20e6949d', 'helpManual', '/images', 'REST_API_FTL_STEP_2.PNG', '6c288ac4-dc60-44ae-a787-276397a4f50c', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'), 
('3ea2a9eb-81bf-4e89-b904-a29d7e6eb709', 'helpManual', '/images', 'REST_API_FTL_STEP_3.PNG', '951c7d6c-a9b1-48c5-bbd4-baa224c6c8d2', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'), 
('b3e3c0c6-62f4-41f0-9931-3ab2a378e5ee', 'helpManual', '/images', 'REST_API_FTL_STEP_4.PNG', '7ab2027e-494b-4ef7-9a5f-eb3f3af8a6f0', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9');


REPLACE INTO jq_file_upload(file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id) VALUES
('84dfcea6-4f20-44c6-b33e-c76cedbf3053', 'helpManual', '/images', 'REST_API_JAVA_Example.PNG', 'db97403b-02d6-4236-be08-fc0c03676051', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'), 
('f63b06f3-ae5d-48f0-9d9a-0556fa98c8f4', 'helpManual', '/images', 'REST_API_JAVA_STEP_1.PNG', '3ec982f5-9e63-4bc1-9688-ceef23f50147', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'), 
('92f1b314-0c50-4c4b-aaca-39c18f003eed', 'helpManual', '/images', 'REST_API_JAVA_STEP_2.PNG', '3abf27bc-20be-4818-977d-90e339ffc9e4', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'), 
('8f2e0a09-0088-4adf-a224-4f4a50a9a894', 'helpManual', '/images', 'REST_API_JAVA_STEP_3.PNG', '55a87e57-27e2-4e45-9c28-cbd59a6424ab', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'), 
('8984531a-367d-4f25-b3a8-80c7cb9c8066', 'helpManual', '/images', 'REST_API_JAVA_STEP_4.PNG', '14d17671-a791-4989-a9dc-98a9e7377888', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'), 
('512a2273-df0e-40b2-b482-2927aee32c2c', 'helpManual', '/images', 'REST_API_JAVA_STEP_5.PNG', 'ae5e0984-c13b-4e64-9529-6a4857a9ba3a', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'), 
('89ad8745-d78a-4abf-8793-7e7001a288ca', 'helpManual', '/images', 'REST_API_JAVA_STEP_6.PNG', '50e9f2b8-4525-4c6b-bd57-71dd8c7b3653', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'), 
('78827b0f-e616-4352-b70d-11588f712a83', 'helpManual', '/images', 'REST_API_JAVA_STEP_7.PNG', '786d48be-5b7c-4fe8-ad08-1019e3501393', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9');


REPLACE INTO jq_file_upload(file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id) VALUES
('ddefefe2-b9ad-4cac-871f-28cefb134208', 'helpManual', '/images', 'REST_API_JS_STEP_1.PNG', '35e3530d-445f-44d5-a6bc-cccc90719b48', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'), 
('c5ce38d5-b8f6-4ec1-a947-b0db7963812c', 'helpManual', '/images', 'REST_API_JS_STEP_2.PNG', 'ad8d44ef-5d4c-4c50-a0f6-8e6f962b3dde', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'), 
('377f1c16-03a2-4202-81d1-8151be81fb1a', 'helpManual', '/images', 'REST_API_JS_STEP_3.PNG', '9c527c6f-ab95-42e9-9606-594b0ed5aaf3', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'), 
('3009a9cb-dcae-49ba-a3c2-c13ce62750ef', 'helpManual', '/images', 'REST_API_JS_STEP_4.PNG', '9e9452ba-6b47-4aa1-be82-85eee9a93718', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'), 
('2e5939e8-8d84-42b5-9f92-3a9c590e5a98', 'helpManual', '/images', 'REST_API_JS_STEP_5.PNG', 'e723fb52-7fdf-4e2a-9e58-a190d2d033d4', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9');


REPLACE INTO jq_file_upload(file_upload_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_bin_id, file_association_id) VALUES
('7bba0ef5-1416-4d3c-84bc-a48e2d0777ec', '/images', 'validation_message.png', 'ebdc1320-7a4d-440b-9bd4-f0324b0d2894', 'admin@jquiver.io', NOW(), 'helpManual', '50f92287-c438-4ee2-bdfa-b4e710d2e64b'),
('11363af4-6efe-4e18-95dc-8ed6c83033d4', '/images', 'others_1.png', '0abb596e-6ac4-4634-b25f-9d74cc460848', 'admin@jquiver.io', NOW(), 'helpManual', '50f92287-c438-4ee2-bdfa-b4e710d2e64b'),
('881f45cd-2e96-4c62-8b0e-65bc2925dd0b', '/images', 'others_2.png', '4f85084d-419f-4584-88a6-31e190525cac', 'admin@jquiver.io', NOW(), 'helpManual', '50f92287-c438-4ee2-bdfa-b4e710d2e64b'),
('9674702e-c1e5-4a67-8f98-f167871a2207', '/images', 'others_3.png', '75ce0e2f-2020-4831-85ac-f0f1a42afb98', 'admin@jquiver.io', NOW(), 'helpManual', '50f92287-c438-4ee2-bdfa-b4e710d2e64b');

DELETE FROM jq_file_upload WHERE file_upload_id = '1f178e13-be12-4c29-bc47-9910a867af6c';
DELETE FROM jq_file_upload WHERE file_upload_id = 'dc8c094d-700b-40a9-8d7c-49e1aa55e1dd';
DELETE FROM jq_file_upload WHERE file_upload_id = '00b01a2d-bdeb-40aa-b457-169988480295';
DELETE FROM jq_file_upload WHERE file_upload_id = '09f4260c-0cef-4569-b806-7589ee2a108f';
DELETE FROM jq_file_upload WHERE file_upload_id = '6011972c-0544-4322-8bb8-6229463682cb';
DELETE FROM jq_file_upload WHERE file_upload_id = 'c288ea4f-e848-425a-9e1a-c0284d447079';
DELETE FROM jq_file_upload WHERE file_upload_id = '5d4065f3-78d7-4480-be77-ac3f35dff0ff';
DELETE FROM jq_file_upload WHERE file_upload_id = 'b0c436b2-4c76-49f7-b960-a537c09e7d21';
DELETE FROM jq_file_upload WHERE file_upload_id = 'c4fd6ce2-5071-4962-bc8f-56bfce3b2907';
DELETE FROM jq_file_upload WHERE file_upload_id = '9f441835-28b8-425d-b21f-abbced54dfa2';
DELETE FROM jq_file_upload WHERE file_upload_id = 'da04a076-ed62-4f42-b1e8-91372a3e0fc4';
DELETE FROM jq_file_upload WHERE file_upload_id = '6f796eb0-bafa-4dcd-9c8a-d2469be048fe';
DELETE FROM jq_file_upload WHERE file_upload_id = 'f24d2e80-8b38-48a0-bfac-5a2488736086';
DELETE FROM jq_file_upload WHERE file_upload_id = '717c32c4-be30-47d7-b55e-25e11b20b195';
DELETE FROM jq_file_upload WHERE file_upload_id = '59ad8aa2-fcbe-4ff8-85b4-b92fb09d2b81';
DELETE FROM jq_file_upload WHERE file_upload_id = 'fc522253-f73a-4980-a43b-c096d82e6081';
DELETE FROM jq_file_upload WHERE file_upload_id = 'a3f2c013-dd80-4cd0-957f-a3ae8da725e1';
DELETE FROM jq_file_upload WHERE file_upload_id = '3860998e-33ab-4dcb-a27d-0cce0aa73337';
DELETE FROM jq_file_upload WHERE file_upload_id = '2942ed27-f194-459a-8121-0106b9d3d9a4';
DELETE FROM jq_file_upload WHERE file_upload_id = '72d8474a-7d4d-4aa8-8528-833679baf9be';
DELETE FROM jq_file_upload WHERE file_upload_id = 'a3f4f471-0386-40ee-b21b-e91841083822';
DELETE FROM jq_file_upload WHERE file_upload_id = '649fab90-76a9-4135-b271-71b9388140a7';
DELETE FROM jq_file_upload WHERE file_upload_id = 'a3e471fd-f9be-4987-bb4f-9db93a9cbb06';
DELETE FROM jq_file_upload WHERE file_upload_id = '93d0e859-4726-41ea-af6d-743258cbecd1';
DELETE FROM jq_file_upload WHERE file_upload_id = '758ba2cd-ff8a-4865-94b2-4d062ffab93c';
DELETE FROM jq_file_upload WHERE file_upload_id = '553b2332-bf8b-462b-9c5c-d89bc0682a92';
DELETE FROM jq_file_upload WHERE file_upload_id = 'b3fd7730-a501-4a50-afed-54a956151748';
DELETE FROM jq_file_upload WHERE file_upload_id = '577dfda4-41cb-4c8a-ad9d-bdb769140d36';
DELETE FROM jq_file_upload WHERE file_upload_id = 'f207f7fb-0a09-471d-b17d-93f1b55f0f86';
DELETE FROM jq_file_upload WHERE file_upload_id = 'ac0a0f40-cbc8-4f52-8125-a2d7808a2ff4';
DELETE FROM jq_file_upload WHERE file_upload_id = '0a7a4f60-8819-43fd-aa85-90a02bb042ba';
DELETE FROM jq_file_upload WHERE file_upload_id = '4daeff56-7cbc-4f9b-ba29-3c60021ec634';
DELETE FROM jq_file_upload WHERE file_upload_id = '29f15fe9-bd19-4f4a-a4c9-ec198a9b078b';
DELETE FROM jq_file_upload WHERE file_upload_id = '9656a918-91c6-4352-8dbb-07860e404e53';
DELETE FROM jq_file_upload WHERE file_upload_id = 'ecbb9c05-228b-4a0d-b65c-202891905318';
DELETE FROM jq_file_upload WHERE file_upload_id = '44c3eaa9-82a7-4c66-8ba5-af20ab5ff087';
DELETE FROM jq_file_upload WHERE file_upload_id = 'ecb167a8-bb55-4c23-be00-32b88e8903d1';
DELETE FROM jq_file_upload WHERE file_upload_id = '8261a46a-3ece-4ff6-a81e-742ac55d1cfa';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d762c323c01762cc6f25c0001';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d762c323c01762cc7015f0002';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d762c323c01762cc712310003';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d762c323c01762cc71ee00004';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d762c323c01762cc72f480005';
DELETE FROM jq_file_upload WHERE file_upload_id = '1fca4824-251f-4426-abdc-8cff655fbfa4';
DELETE FROM jq_file_upload WHERE file_upload_id = '3a1e89b9-f0cb-42aa-ab7d-041e08daf536';
DELETE FROM jq_file_upload WHERE file_upload_id = '4ab78613-7f7b-46b5-af26-73a3643aaea0';
DELETE FROM jq_file_upload WHERE file_upload_id = '8b01d2d4-1bd8-4a54-a460-f50202cd0471';
DELETE FROM jq_file_upload WHERE file_upload_id = '72ad938d-a6de-4d16-9203-27cc8b12f649';
DELETE FROM jq_file_upload WHERE file_upload_id = '1bd91c6b-3f30-4481-8803-f9a8fe8b8626';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d7629c27501762a22257f0012';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d7629c27501762a22341b0013';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d7629c27501762a223f2e0014';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d7629c27501762a2258450015';
DELETE FROM jq_file_upload WHERE file_upload_id = '40281694762864a50176288c66620001';
DELETE FROM jq_file_upload WHERE file_upload_id = '40281694762864a50176288c755a0002';
DELETE FROM jq_file_upload WHERE file_upload_id = '402816947628cb18017628cfa6110000';
DELETE FROM jq_file_upload WHERE file_upload_id = 'c6b5c730-804f-4aab-9a22-ea8d2b32ad63';
DELETE FROM jq_file_upload WHERE file_upload_id = '29792cef-d7e7-43ba-a15a-36ec20e6949d';
DELETE FROM jq_file_upload WHERE file_upload_id = '3ea2a9eb-81bf-4e89-b904-a29d7e6eb709';
DELETE FROM jq_file_upload WHERE file_upload_id = 'b3e3c0c6-62f4-41f0-9931-3ab2a378e5ee';
DELETE FROM jq_file_upload WHERE file_upload_id = '6079c4c4-167a-4a16-bdee-56c9f93f986b';
DELETE FROM jq_file_upload WHERE file_upload_id = 'f63b06f3-ae5d-48f0-9d9a-0556fa98c8f4';
DELETE FROM jq_file_upload WHERE file_upload_id = '92f1b314-0c50-4c4b-aaca-39c18f003eed';
DELETE FROM jq_file_upload WHERE file_upload_id = '8f2e0a09-0088-4adf-a224-4f4a50a9a894';
DELETE FROM jq_file_upload WHERE file_upload_id = '8984531a-367d-4f25-b3a8-80c7cb9c8066';
DELETE FROM jq_file_upload WHERE file_upload_id = '512a2273-df0e-40b2-b482-2927aee32c2c';
DELETE FROM jq_file_upload WHERE file_upload_id = '89ad8745-d78a-4abf-8793-7e7001a288ca';
DELETE FROM jq_file_upload WHERE file_upload_id = '78827b0f-e616-4352-b70d-11588f712a83';
DELETE FROM jq_file_upload WHERE file_upload_id = '84dfcea6-4f20-44c6-b33e-c76cedbf3053';
DELETE FROM jq_file_upload WHERE file_upload_id = '2e5939e8-8d84-42b5-9f92-3a9c590e5a98';
DELETE FROM jq_file_upload WHERE file_upload_id = 'ddefefe2-b9ad-4cac-871f-28cefb134208';
DELETE FROM jq_file_upload WHERE file_upload_id = 'c5ce38d5-b8f6-4ec1-a947-b0db7963812c';
DELETE FROM jq_file_upload WHERE file_upload_id = '377f1c16-03a2-4202-81d1-8151be81fb1a';
DELETE FROM jq_file_upload WHERE file_upload_id = '3009a9cb-dcae-49ba-a3c2-c13ce62750ef';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d765124480176512a5e0f0000';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d765124480176512a6e660001';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d765124480176512a79560002';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d765124480176512a89830003';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d765124480176512aa3f80005';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d765124480176512a96630004';
DELETE FROM jq_file_upload WHERE file_upload_id = '6cdc83c4-e2b2-4eb8-b9cb-17aff7c51a58';
DELETE FROM jq_file_upload WHERE file_upload_id = '9e9fcdb7-f54d-4c04-aee5-76a35a0559d8';
DELETE FROM jq_file_upload WHERE file_upload_id = '5ea356bb-88d3-4097-b44d-d96ec273ed2f';
DELETE FROM jq_file_upload WHERE file_upload_id = 'c3779826-2f8b-42d1-afeb-8302482f468f';
DELETE FROM jq_file_upload WHERE file_upload_id = '97df2f91-924d-4d86-b999-72f43bf4c2bc';
DELETE FROM jq_file_upload WHERE file_upload_id = 'd560cd3c-82f8-4c7b-927b-77c679cdcf51';
DELETE FROM jq_file_upload WHERE file_upload_id = '8d1522cb-a12a-474e-a870-2d63098b620b';
DELETE FROM jq_file_upload WHERE file_upload_id = 'e8534c20-a6cf-456a-82bc-8290c4a14792';
DELETE FROM jq_file_upload WHERE file_upload_id = '66b7db46-3bbf-40fc-b0aa-24ae8e3febba';
DELETE FROM jq_file_upload WHERE file_upload_id = 'b0313ee9-32b5-42bc-8b8c-9ad79c942c42';
DELETE FROM jq_file_upload WHERE file_upload_id = '68d76985-5616-4fb4-a481-b25fd6ccf888';
DELETE FROM jq_file_upload WHERE file_upload_id = 'a3f74af1-8159-4134-942f-5d4655d4203e';
DELETE FROM jq_file_upload WHERE file_upload_id = 'a6b9c378-6ffc-4874-870e-271d829103cf';
DELETE FROM jq_file_upload WHERE file_upload_id = 'f625d2c2-6073-46a3-adc0-64d903aeaaa7';
DELETE FROM jq_file_upload WHERE file_upload_id = 'a501ef3d-bcc9-4d42-8990-99ddeb97fbe4';
DELETE FROM jq_file_upload WHERE file_upload_id = '88260a66-eaaf-40fb-b383-518e131cb6f9';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d762c323c01762cc6c8a30000';
DELETE FROM jq_file_upload WHERE file_upload_id = 'f7fed392-84c0-496b-8aa8-3a6f38d9b71e';
DELETE FROM jq_file_upload WHERE file_upload_id = '4028168b7647e50a0176488ec8860003';
DELETE FROM jq_file_upload WHERE file_upload_id = '4028168b7647e50a0176488f32110004';
DELETE FROM jq_file_upload WHERE file_upload_id = '4028168b7647e50a0176488f4ebe0005';
DELETE FROM jq_file_upload WHERE file_upload_id = '4028168b7647e50a0176488f60ba0006'; 
DELETE FROM jq_file_upload WHERE file_upload_id = '4028b88176ae0bd20176ae160c010000';
DELETE FROM jq_file_upload WHERE file_upload_id = '4028b88176a8f81a0176a9ae0cf80001';
DELETE FROM jq_file_upload WHERE file_upload_id = '4028b88176a8f81a0176a9aad1090000';
DELETE FROM jq_file_upload WHERE file_upload_id = '4028b88176ae0bd20176ae1abd670002';
DELETE FROM jq_file_upload WHERE file_upload_id = '4028b88176ae0bd20176ae1843480001';
DELETE FROM jq_file_upload WHERE file_upload_id = '4028b88176ae0bd20176ae1bbc760003';
DELETE FROM jq_file_upload WHERE file_upload_id = '8f27a0a6-2417-4078-b834-f3d04fc9e9ee';
DELETE FROM jq_file_upload WHERE file_upload_id = '9b8b6f1c-a684-4892-a4f3-769ef9ee5230';
DELETE FROM jq_file_upload WHERE file_upload_id = 'bfc607ae-6570-4188-84d5-203246bfe85a';
DELETE FROM jq_file_upload WHERE file_upload_id = 'f41f8189-16ae-4b85-9344-563443e8d70c';
DELETE FROM jq_file_upload WHERE file_upload_id = 'f0879990-0718-40ed-a028-977d20df125c';
DELETE FROM jq_file_upload WHERE file_upload_id = 'fd38c7b1-2ff4-4c19-b3ca-c1c35f70ea03';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d763887410176388b36b60001';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d763887410176388995320000';
DELETE FROM jq_file_upload WHERE file_upload_id = '5d45f43e-5ba0-471a-8fa5-80de72d12eb0';
DELETE FROM jq_file_upload WHERE file_upload_id = '4028b8817646ed030176480e43ab0026';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d8f76a600980176a60bfa870002';
DELETE FROM jq_file_upload WHERE file_upload_id = '4028b8817650f1220176511211c40002';
DELETE FROM jq_file_upload WHERE file_upload_id = '4028b8817650f12201765116077e0003';
DELETE FROM jq_file_upload WHERE file_upload_id = '4028b8817650f122017651166e0d0004';
DELETE FROM jq_file_upload WHERE file_upload_id = '4028b8817650f12201765122f1750007';
DELETE FROM jq_file_upload WHERE file_upload_id = '4028b8817650f12201765121c1300005';
DELETE FROM jq_file_upload WHERE file_upload_id = '4028b8817650f122017651226ab90006';
DELETE FROM jq_file_upload WHERE file_upload_id = '4028b881765fd657017660116dac0000';
DELETE FROM jq_file_upload WHERE file_upload_id = '4028b8817646ed03017647ef2f050022';
DELETE FROM jq_file_upload WHERE file_upload_id = '4028b8817646ed030176480a3c080024';
DELETE FROM jq_file_upload WHERE file_upload_id = '4028b8817646ed03017647dc3ab40021';
DELETE FROM jq_file_upload WHERE file_upload_id = '4028b8817646ed030176480ce3fd0025';
DELETE FROM jq_file_upload WHERE file_upload_id = '4028b8817646ed03017647f1c1330023';

DELETE FROM jq_file_upload WHERE file_upload_id = '03f110de-811d-47f3-be2e-fc6196c11ace';
DELETE FROM jq_file_upload WHERE file_upload_id = '8c901fa8-4c71-48dd-bd1c-f2e6bdba2fce';
DELETE FROM jq_file_upload WHERE file_upload_id = 'a318aa76-ce66-446e-8418-fdbc62ede7c8';
DELETE FROM jq_file_upload WHERE file_upload_id = 'f288f186-d985-4a53-abb1-4b293ef92bf1';
DELETE FROM jq_file_upload WHERE file_upload_id = '05777b2b-c424-4472-8678-363d4fe3ac5d';
DELETE FROM jq_file_upload WHERE file_upload_id = '62765d11-c5ad-4128-a94a-f55ac5b36bbe';
DELETE FROM jq_file_upload WHERE file_upload_id = '6908081e-3441-40f0-87db-3e55f00feb83';
DELETE FROM jq_file_upload WHERE file_upload_id = '6d2b0993-be4c-43c8-a72f-19eaf5a6ee93';
DELETE FROM jq_file_upload WHERE file_upload_id = '74909e25-3efa-41c7-a11f-8e02dcc1768c';
DELETE FROM jq_file_upload WHERE file_upload_id = '8606b360-59e0-469f-86ea-737605981c04';
DELETE FROM jq_file_upload WHERE file_upload_id = 'b71984b5-841b-4b6e-bffa-77ca956105dc';
DELETE FROM jq_file_upload WHERE file_upload_id = 'c57e84f0-aa52-4597-98d8-9eb2afd90cc5';

DELETE FROM jq_file_upload WHERE file_upload_id = '402816947628e22d017628e709eb0000';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d7650c0a1017650c8fccf0001';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d7650c0a1017650c90cce0002';
DELETE FROM jq_file_upload WHERE file_upload_id = '402816947628e22d017628e71f020001';
DELETE FROM jq_file_upload WHERE file_upload_id = '402816947628e22d017628ea39e00002';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d7650c0a1017650c8ef410000';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d7629c275017629c35c830000';

DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d7629c27501762a0eb728000a';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d7629c27501762a0ee8f3000b';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d76509b1e017650ae7d4d0015';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d7629c27501762a0f3e5a000c';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d7629c27501762a0f8466000d';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d7629c27501762a0f914b000e';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d7629c27501762a0f9c51000f';

DELETE FROM jq_file_upload WHERE file_upload_id = '40288089766b4ac701766b61fa4a0019';
DELETE FROM jq_file_upload WHERE file_upload_id = '40288089766b4ac701766b5f263d0017';
DELETE FROM jq_file_upload WHERE file_upload_id = '40288089766a9a9501766acfa31b0002';
DELETE FROM jq_file_upload WHERE file_upload_id = '40288089766a9a9501766ad0ba300004';
DELETE FROM jq_file_upload WHERE file_upload_id = '40288089766a9a9501766acfe7920003';

DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d7660efd0017660f9a518000b';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d765d4f9c01765d7279a50009';
DELETE FROM jq_file_upload WHERE file_upload_id = '402816927661a061017661a88a860006';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d7661398f0176617e3aee0004';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d765d4f9c01765d7287ee000a';

DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d7660efd0017660f9ba53000c';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d765d4f9c01765d67e8320007';
DELETE FROM jq_file_upload WHERE file_upload_id = '402816927661a061017661a86f8c0004';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d7661398f0176617633730002';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d765d4f9c01765d67f5d90008';

DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d765d4f9c01765d5514740000';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d7660efd0017660f91702000a';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d765d4f9c01765d67c7d90005';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d7661398f0176615d9dbd0001';
DELETE FROM jq_file_upload WHERE file_upload_id = '4028168b7647e50a017648ca0c690009';
DELETE FROM jq_file_upload WHERE file_upload_id = '40289d3d765d4f9c01765d67d7850006';

REPLACE INTO jq_file_upload(file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id) VALUES
('6d1317e0-2c6d-42cc-a114-87e35a325214', 'helpManual', '/images', 'Router_1.png', '9d9d3383-7fef-45d6-a4c6-765ac8746126', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('5e749fe5-4090-42f1-aafa-72e017d3d170', 'helpManual', '/images', 'Add_Route.png', '93b7d279-433e-4f59-ad2f-69d4176a0261', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('8a665c76-6ea7-4f82-a4e4-1b5401a601a5', 'helpManual', '/images', 'Add_Route_1.png', '490ac986-b080-4e27-ad6d-f9a7a97e44ee', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('a56833f3-0ee3-42ff-8311-aef0375f89a1', 'helpManual', '/images', 'Add_Route_2.png', '2bf0ee5e-cd41-4382-b52a-2b6c0d260528', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('c416c1d5-465c-49e2-96b9-7cc10459f4b2', 'helpManual', '/images', 'Route_Dashboard.png', 'e31a6e54-7d29-4ecd-ac29-1ffade1768fa', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('0b75821b-3fc6-4c90-a439-5b58cae08edf', 'helpManual', '/images', 'Route_Dashboard_1.png', '263581ea-4f4c-4cce-8f49-f1577865211a', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('40f73b6e-c35f-45bc-86a5-683f8b60b668', 'helpManual', '/images', 'Route_Dashboard_2.png', '31e6bdba-dc36-49bb-b02e-792665cdb065', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('8e1d8100-d202-487f-a925-b18a7b3c803d', 'helpManual', '/images', 'Route_Dashboard_3.png', '209074f0-f4ed-4f05-a53c-a9a4e57fadea', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('d5c00cf7-9caa-4102-a33c-57bdbff31901', 'helpManual', '/images', 'Route_Dashboard_4.png', '118b4619-f92a-46f9-928d-e484b3cdc030', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('ff125cd6-db4e-483c-8ada-be6b75c690d5', 'helpManual', '/images', 'Route_FormBuilder.png', '7fd3aaee-6084-445a-8363-c346740dde0f', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('d1085c0f-a2c0-42ca-a760-3ebfaa528059', 'helpManual', '/images', 'Route_FormBuilder_1.png', 'c61c1f00-0dc1-471b-b1ae-1e12b1f00f84', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('3c9b48c8-e289-4dc0-adcb-d65d15ca585a', 'helpManual', '/images', 'Route_FormBuilder_2.png', '04064def-a252-4dea-b7fb-426bf9cd1175', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('78162486-74c6-4312-a34b-f96e099e8896', 'helpManual', '/images', 'Route_FormBuilder_3.png', 'dba517f7-46c1-4e3e-a49d-dcba4f5f2093', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('ee3b19bf-fa9f-446a-baac-0cd88c7da3e3', 'helpManual', '/images', 'Route_FormBuilder_4.png', 'b7713cb5-c0c7-45ad-bc34-30a968dce457', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('d9dc6447-f608-4b8a-9350-d24dd4bd483e', 'helpManual', '/images', 'Route_RestAPI_1.png', '5aecadd5-147c-4f13-90ea-0b88fd2beeda', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('815a77bf-67cc-4232-81ba-3a162508e55d', 'helpManual', '/images', 'Route_RestAPI_2.png', '0e748a99-bed8-48a3-afea-16961415e4a4', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('23db4a30-f453-469d-b4a5-ddf1947906e6', 'helpManual', '/images', 'Route_RestAPI_3.png', '1c8112f2-7521-481c-b771-8f21378cf780', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('1151237b-2e22-4211-a906-bf541daa90fa', 'helpManual', '/images', 'Route_RestAPI_4.png', '7feaa6ce-ebfe-4ff0-b51c-2dd5a585c90e', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('41b6419b-a5db-4cb3-85b9-8ad9bbac1d72', 'helpManual', '/images', 'Route_RestAPI_5.png', '9fafb19d-0b1d-4c14-be2b-f10d8fbc47f2', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('ee3b19bf-fa9f-446a-baac-0cd88c7da3e3', 'helpManual', '/images', 'Route_FormBuilder_4.png', 'b7713cb5-c0c7-45ad-bc34-30a968dce457', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('bde187ec-841d-4bc6-a6d4-9b190522719b', 'helpManual', '/images', 'Route_Template.png', '19c8d9f1-48c1-4d3e-a169-3c951d415712', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('ef034de4-f415-46e6-be68-e7a70525f51f', 'helpManual', '/images', 'Route_Template_1.png', 'ea81eaf5-ee19-490f-b88f-7b756902e4ef', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('e6c3766c-3674-4c06-ac7a-811e3fafa293', 'helpManual', '/images', 'Route_Template_2.png', 'd5a05749-9107-47f2-b9e1-2f06757ee723', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('5d011541-3b89-4cf7-806d-7c665d4dd678', 'helpManual', '/images', 'Route_Template_3.png', '8c771814-f8df-435e-b9de-6592c4157d28', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('313275b4-09e3-4c83-9b00-f2cd31771347', 'helpManual', '/images', 'Route_Template_4.png', '8c98436a-02c7-4c92-8f87-1e246d20df7b', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('ba120df8-9cd3-447b-8068-62f363d9d99f', 'helpManual', '/images', 'Route_Root.png', '71778e56-9e7b-4f47-aa85-c9e337d38fe7', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('c1bc0488-b628-48b9-96d2-98305c726526', 'helpManual', '/images', 'Route_Root_1.png', 'b9ae9bb3-8674-4df5-b78d-6f67106af0f0', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('4b9b0d54-f4d2-44d7-80be-a63fac673d97', 'helpManual', '/images', 'Route_Root_2.png', 'd5ab9c9b-8d25-4eb9-b024-92a5469dd8df', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('053515e0-21fc-457c-9a42-7049c03109ae', 'helpManual', '/images', 'Route_Root_3.png', '79727812-1148-4302-bd7a-bca44c359de5', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('c47b8aa7-3919-405a-84b7-01f572fc38d8', 'helpManual', '/images', 'Route_Role.png', '917f2761-ab02-47b8-b12a-274d325aeec6', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('94393d36-1bf8-4083-9d2c-16de4de16657', 'helpManual', '/images', 'Route_Role_1.png', '6744e1f6-0a29-4f1e-9e33-34eac5fe114c', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('0eda2adf-7cf0-4dd0-95f3-77ed50af61ef', 'helpManual', '/images', 'Route_Role_2.png', '12bcdf34-1e8d-4eab-bc8f-c3de4fbfedc7', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('a29aac02-f32f-4753-8709-2f5f46e852cb', 'helpManual', '/images', 'Route_role_3.png', '421458ac-cca1-4e85-b375-cf1a1e1af79b', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('23295c6d-0bab-47fd-872a-10214f944179', 'helpManual', '/images', 'Route_Role_4.png', '1a0a17ce-0cd0-4b6d-aea2-8a48a35379d4', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('2e52c447-9816-4436-9b3d-c369eba9b5b5', 'helpManual', '/images', 'Route_Role_5.png', '85ce08cb-ca22-45e4-9532-bac8be81db81', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('3a611f48-4953-45b5-a7e4-89d0d9357f2c', 'helpManual', '/images', 'Route_Role_6.png', '4577208e-6de2-4296-93a4-5ca46d393eae', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('92ab2130-8770-4642-aec9-7919bb4cfa7d', 'helpManual', '/images', 'Route_User_2.png', 'b75abc86-5ad1-4a3c-95c3-369aa263f7f2', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('d9051688-0939-493f-b822-0004858426c5', 'helpManual', '/images', 'Route_User_1.png', 'ddb4df56-1489-4da2-855a-0b66e5c53cc9', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('535b61a7-7ade-46c0-903b-f8c3caf7c57a', 'helpManual', '/images', 'Route_User.png', 'bfdc7dd7-a422-4185-b9f7-c754042a3944', 'admin@jquiver.io', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),

('f189cae9-b979-4525-b268-585cfd41c6e9', 'helpManual', '/images', 'Internationalization_1.png', '19857863-72bf-4271-b9d3-8950e7bb8dcd', 'admin@jquiver.io', NOW(), '9c25fb63-8336-4f22-bb97-a5042159d5c4'),
('23cd6bae-7411-48a1-a0df-6251d46e90ad', 'helpManual', '/images', 'Internalization_2.png', '059bb1cd-7912-48d4-910d-c86618d8780d', 'admin@jquiver.io', NOW(), '9c25fb63-8336-4f22-bb97-a5042159d5c4'),
('af7e0224-7882-4abd-98b1-8e2cfeb0080c', 'helpManual', '/images', 'Internalization_3.png', 'd4026b7b-2329-4a5b-b996-3a926095691a', 'admin@jquiver.io', NOW(), '9c25fb63-8336-4f22-bb97-a5042159d5c4'),
('04643fd0-0f99-48e2-9cb6-585339a10fbd', 'helpManual', '/images', 'Internalization_4.png', 'b999088e-3de8-4b63-b80a-c46565c81e44', 'admin@jquiver.io', NOW(), '9c25fb63-8336-4f22-bb97-a5042159d5c4'),
('f1147460-9a8f-42da-8740-358ed18dd6b9', 'helpManual', '/images', 'Internalization_5.png', '277ee80e-c2cf-4cb4-a209-9d933d1ecd98', 'admin@jquiver.io', NOW(), '9c25fb63-8336-4f22-bb97-a5042159d5c4'),
('8febc6d9-8cee-4c46-9776-96618679b4a3', 'helpManual', '/images', 'Internalization_6.png', 'b1900d69-4c98-4b5c-93e4-1ee2ddb49ea3', 'admin@jquiver.io', NOW(), '9c25fb63-8336-4f22-bb97-a5042159d5c4'),
('400b7c6d-b9cd-4d02-a7dc-258a3ce438c2', 'helpManual', '/images', 'Internalization_7.png', 'c59ba99e-07ec-4801-9a2d-3e2d5a9dd21b', 'admin@jquiver.io', NOW(), '9c25fb63-8336-4f22-bb97-a5042159d5c4'),
('f86dd4f2-8e9c-4cea-b060-17e9a90f4595', 'helpManual', '/images', 'Internalization_8.png', 'a522eb76-29d1-4aad-83a5-542eb259d4c7', 'admin@jquiver.io', NOW(), '9c25fb63-8336-4f22-bb97-a5042159d5c4'),
('2b19fde4-4052-4795-add9-49d6f11f0876', 'helpManual', '/images', 'Internalization_9.png', '0ac1a05f-4ef8-4c52-a1e2-b07ed2a18880', 'admin@jquiver.io', NOW(), '9c25fb63-8336-4f22-bb97-a5042159d5c4'),
('2a69ddbe-6cf6-4a0f-9968-824fee323719', 'helpManual', '/images', 'Internalization_10.png', '94233cbe-eaa8-4782-8ab8-b3c0f5d335ee', 'admin@jquiver.io', NOW(), '9c25fb63-8336-4f22-bb97-a5042159d5c4'),
('e72998bd-d7c8-4d5a-a184-e2cb2a47cd5e', 'helpManual', '/images', 'Internalization_11.png', '01b79fe9-2bb1-444e-ae1b-eb199f793231', 'admin@jquiver.io', NOW(), '9c25fb63-8336-4f22-bb97-a5042159d5c4'),
('e4610ca0-516d-42f9-bd7a-ea2dee30884c', 'helpManual', '/images', 'Internalization_12.png', '6246901e-404b-46a9-9d67-e9c2745c2f36', 'admin@jquiver.io', NOW(), '9c25fb63-8336-4f22-bb97-a5042159d5c4'),
('a9531ffe-e909-4d3a-98cf-34ce2aac2237', 'helpManual', '/images', 'Internalization_13.png', 'ee0550c4-28d8-4c13-a256-c45ee8f66508', 'admin@jquiver.io', NOW(), '9c25fb63-8336-4f22-bb97-a5042159d5c4'),
('206529b7-eba9-4004-b1c3-3ca93e8c0f1b', 'helpManual', '/images', 'Internalization_14.png', '287f77ab-44e0-4fe8-848b-7a19c39a107f', 'admin@jquiver.io', NOW(), '9c25fb63-8336-4f22-bb97-a5042159d5c4'),
('425f0a7c-16d2-49ab-8484-6e14bb59d677', 'helpManual', '/images', 'Internalization_15.png', '6e837647-8c3b-445b-80aa-1fe3f6c81c1d', 'admin@jquiver.io', NOW(), '9c25fb63-8336-4f22-bb97-a5042159d5c4'),
('7174b451-be2d-486b-bbe1-5e27788654a9', 'helpManual', '/images', 'Internalization_16.png', 'acdf0d2c-fb5a-40b4-b429-bfeee43653f1', 'admin@jquiver.io', NOW(), '9c25fb63-8336-4f22-bb97-a5042159d5c4'),
('2d8f96e4-5254-4741-ac6f-3f3666784f04', 'helpManual', '/images', 'Internalization_17.png', '98826059-7bb6-435e-996f-6ec4bf347a2d', 'admin@jquiver.io', NOW(), '9c25fb63-8336-4f22-bb97-a5042159d5c4'),
('bf77f351-468f-4c4b-8af9-e68712589fb0', 'helpManual', '/images', 'Internalization_18.png', '5d3b6ee8-b26b-42e3-baf9-6283fc2007f1', 'admin@jquiver.io', NOW(), '9c25fb63-8336-4f22-bb97-a5042159d5c4'),
('c37e67a1-aa74-4525-b42d-66a5c236b4d8', 'helpManual', '/images', 'Internalization_19.png', '874cf572-4fa1-4f4c-97cc-d941c6f5b1f7', 'admin@jquiver.io', NOW(), '9c25fb63-8336-4f22-bb97-a5042159d5c4'),

('fec458a2-cc61-4ccb-890b-715f94930afe', 'helpManual', '/images', 'Grid.png', '063cb7bc-2b19-4ff8-9d74-8740e7d23f2c', 'admin@jquiver.io', NOW(), '7428a452-da97-4ef0-b6d7-acf4921beb82'),
('16afb46c-8249-4e46-975e-ad27250792f1', 'helpManual', '/images', 'Grid_1.png', 'cdeb1d7e-10c8-4b5e-bd60-e11c6bd68870', 'admin@jquiver.io', NOW(), '7428a452-da97-4ef0-b6d7-acf4921beb82'),
('18d495df-c22f-4976-8b7d-82a114e650e9', 'helpManual', '/images', 'Grid_2.png', '4700ebeb-2c98-4bc1-88b1-a6647a9f7b55', 'admin@jquiver.io', NOW(), '7428a452-da97-4ef0-b6d7-acf4921beb82'),
('1fb8573a-5272-450e-9e1a-a88337c8c5a4', 'helpManual', '/images', 'Grid_3.png', 'cbf40e7c-22a8-4c07-b562-1b02619b60e4', 'admin@jquiver.io', NOW(), '7428a452-da97-4ef0-b6d7-acf4921beb82'),

('6469c5d5-eb34-4199-90f2-caa40138dd57', 'helpManual', '/images', 'Rest_API_1.png', '58abc44e-5669-4823-8944-5c69cb23c747', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'),
('918a607b-753c-43d8-955c-383b70629fae', 'helpManual', '/images', 'Rest_API_2.png', '5d62680f-5684-446e-afa6-b27e4ac284c0', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'),
('570e9aad-b89b-4a03-b08b-07df318e99db', 'helpManual', '/images', 'Rest_API_3.png', '8fa3456b-eec4-44de-bb43-03b32314b0c3', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'),
('1904c6f2-9e2f-401a-92ef-24f56e8698e2', 'helpManual', '/images', 'Rest_API_4.png', '36c721bd-5137-4ce8-89a0-4d439f2355e0', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'),
('4738696e-a45d-436d-b9fc-8d0d582444ef', 'helpManual', '/images', 'Rest_API_5.png', 'd8b2f796-9926-464d-9289-fb57e36347a5', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'),
('78a189bb-a01c-4916-89c5-4f7b97d39a58', 'helpManual', '/images', 'Rest_API_9.png', '0e990c8b-5d9e-4064-9f88-f00b767adc9c', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'),
('0abe0a6d-a11f-428f-92e2-0c5b1219569b', 'helpManual', '/images', 'Rest_API_10.png', 'e80a914a-5863-439c-8bbb-e73283981dc0', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'),
('c19d68cd-83ba-4888-928e-4518c3481b7c', 'helpManual', '/images', 'Rest_API_11.png', 'a338e692-70a9-43bd-b02a-3a737559ad0b', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'),
('13c874e0-0704-47f8-96a2-67a16b7df3f0', 'helpManual', '/images', 'Rest_API_12.png', 'a4009876-4268-4e0a-9810-397df936ee9c', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'),
('566052ac-cf76-49ce-b213-dfc384f74923', 'helpManual', '/images', 'Rest_API_13.png', 'c40e6939-c011-44b7-8d4d-b34bff069471', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'),
('0e132c56-a984-45da-b2d0-f84982970a02', 'helpManual', '/images', 'Rest_API_14.png', '6862399a-6e15-44de-b349-334862a75aa9', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'),
('bfbf0453-5edf-496a-bbec-dee959a34ef7', 'helpManual', '/images', 'Rest_API_18.png', '756e0d65-12c4-4a24-b98c-e16b98590ff9', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'),

('d9b6dd66-0f0a-40e6-8cef-15c6efa861f3', 'helpManual', '/images', 'Rest_API_19.png', '9671514a-07b6-4adf-8e7a-f32d50af9614', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'),
('79e37ea0-6b50-45fd-97a2-dde0937b0b51', 'helpManual', '/images', 'Rest_API_20.png', '6102f50a-49da-4498-bb34-a4a7bd83d2cf', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'),
('26d47021-c664-4d5f-aab9-89c5a0281a08', 'helpManual', '/images', 'Rest_API_21.png', '43483471-0e03-4bc2-8afa-c40e75a4ba4b', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'),
('57a80913-7368-4797-a46d-248797499a4d', 'helpManual', '/images', 'Rest_API_22.png', 'cf9ae6f1-ab95-45bf-9971-a2825cd7b656', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'),
('79f916c5-3d09-4443-ae5e-871ef468e1b1', 'helpManual', '/images', 'Rest_API_23.png', '3667d6c5-0bc3-4165-b31b-de4236ccb651', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'),
('92f68954-937a-4f66-b1b2-c0edf78c3d31', 'helpManual', '/images', 'Rest_API_24.png', '7b516740-2065-4c6a-b7dd-c419d0fbcd36', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'),
('1144b319-c78f-48bf-b3ee-d39d7706ff08', 'helpManual', '/images', 'Rest_API_25.png', '49a1afeb-2187-4573-b4ff-1071af738e05', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'),
('f2e94f83-fe1b-4c10-accb-585c6dc78a5c', 'helpManual', '/images', 'Rest_API_27.png', 'ae8d35e9-9651-4ef2-81c3-0d6232259d44', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'),
('87ed30d9-203f-45e2-b28a-3d2fe1393225', 'helpManual', '/images', 'Rest_API_28.png', '5d8eb25e-12ba-4968-bc12-2f14d6c02844', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'),

('a98f62e8-7edf-497a-9f7e-d1ea80412e4c', 'helpManual', '/images', 'Rest_API_29.png', '5b2ea009-1832-4445-a28e-1826c24f4e86', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'),
('cf5967b0-e7a9-4617-9796-5d09c618101f', 'helpManual', '/images', 'Rest_API_30.png', '87d64bdc-3477-4ea7-ab84-bee7dfe1380d', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'),
('7de7f045-464c-4833-81be-31a156e362ff', 'helpManual', '/images', 'Rest_API_31.png', 'b316ccb0-d48c-4e69-a07f-a5862d1c58a0', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'),
('863e01cc-d650-4b38-8aae-5d9fd77ee9a9', 'helpManual', '/images', 'Rest_API_32.png', '4442834c-b0e8-432e-8142-51870f1a646d', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'),
('7ae6c1e4-b19e-462c-992a-5da74fa8d96c', 'helpManual', '/images', 'Rest_API_33.png', '22663676-0816-4057-95ff-54649da3fe6e', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'),
('86cfa8fe-54d3-449e-baf0-2b9e7958fd62', 'helpManual', '/images', 'Rest_API_34.png', '926d6a02-9d25-49ba-8940-b7dc50e9f496', 'admin@jquiver.io', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'),

('8f27a0a6-2417-4078-b834-f3d04fc9e9ee', 'helpManual', '/images', 'AutoComplete_Example_1.png', '28a5bc21-8279-4236-9e99-95e4e5fd428f', 'admin@jquiver.io', NOW(), '5e46df00-e07a-4b73-889f-2894adfd3df8'),
('9b8b6f1c-a684-4892-a4f3-769ef9ee5230', 'helpManual', '/images', 'AutoComplete_Example_2.png', '876def69-7981-4347-9c08-69d6f8dc9d77', 'admin@jquiver.io', NOW(), '5e46df00-e07a-4b73-889f-2894adfd3df8'),
('bfc607ae-6570-4188-84d5-203246bfe85a', 'helpManual', '/images', 'AutoComplete_Example_3.png', '08cccd10-6aa4-4b9e-8d65-e1cabdad7076', 'admin@jquiver.io', NOW(), '5e46df00-e07a-4b73-889f-2894adfd3df8'),
('f41f8189-16ae-4b85-9344-563443e8d70c', 'helpManual', '/images', 'AutoComplete_Example_4.png', 'fec68e95-c637-461b-bbee-a5ee5cca3a90', 'admin@jquiver.io', NOW(), '5e46df00-e07a-4b73-889f-2894adfd3df8'),
('f0879990-0718-40ed-a028-977d20df125c', 'helpManual', '/images', 'AutoComplete_Example_5.png', '77b7397b-e1da-41f5-9f7b-1ebed47e4f30', 'admin@jquiver.io', NOW(), '5e46df00-e07a-4b73-889f-2894adfd3df8'),
('fd38c7b1-2ff4-4c19-b3ca-c1c35f70ea03', 'helpManual', '/images', 'AutoComplete_Example_6.png', 'c9b8d064-ff25-4e09-b94b-2092f2e02160', 'admin@jquiver.io', NOW(), '5e46df00-e07a-4b73-889f-2894adfd3df8'),

('83cb09ff-9977-44ce-b452-a2dba3b49660', 'helpManual', '/images', 'Master_Gen_1.png', '00ea1bb9-5ff6-46fd-a8c8-dfecb0dcdfaf', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('37329146-291f-4715-ba85-4e8817da203b', 'helpManual', '/images', 'Master_Gen_2.png', '42dba4c4-06ba-462e-9c76-10e4b3f02390', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('83eb3548-6860-4f0f-bbb6-c40520a84436', 'helpManual', '/images', 'Master_Gen_3.png', '579e4f4f-8348-4486-a825-49acd71b2a9c', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('6c2c11bd-1327-46fb-aac9-5860d1660e76', 'helpManual', '/images', 'Master_Gen_4.png', '2f40059d-6a54-4272-abd1-4d62e3f91e6f', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('ef2f1bd0-765e-468a-80b2-d72fc2dbc436', 'helpManual', '/images', 'Master_Gen_5.png', '787a7037-de7b-40ce-82de-a04a1ac3abc0', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('9b4ee2f0-b396-49bc-ba4b-05cda7dde900', 'helpManual', '/images', 'Master_Gen_6.png', 'bdf8d3c6-2d99-4d44-bba9-5b57b92c663f', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('7f6c9ed8-4827-4b12-97da-c1096976ca6f', 'helpManual', '/images', 'Master_Gen_7.png', '4e39b483-9c7d-443a-b69d-45c0a9eebeeb', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('3b42f9b0-1cc5-4e43-8381-9dcf29e8bf64', 'helpManual', '/images', 'Master_Gen_8.png', '2ee71f9b-1665-48c7-a245-060d44447585', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('61660baf-f11b-40fb-92a1-523ea72153f2', 'helpManual', '/images', 'Master_Gen_9.png', '0a0bc391-5f86-402a-9655-2d5f4e3d21c3', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('bfe0ff63-c318-4f3c-a7dd-8f6196bb309f', 'helpManual', '/images', 'Master_Gen_10.png', '9e1972db-e2f9-435b-a744-2757486967f7', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('63e3a766-0041-4a23-818e-1ccc4bfd2af3', 'helpManual', '/images', 'Master_Gen_11.png', '63f3c4b4-bdad-4cde-b31e-0ea688646006', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('81fd4610-eb9a-43f8-b372-bc5611a9aeb2', 'helpManual', '/images', 'Master_Gen_12.png', '3fcba652-e02a-45a6-9449-5d56eaaf772c', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('cda41ffd-11d1-469f-946e-d05125124084', 'helpManual', '/images', 'Master_Gen_13.png', '918f3a41-0f68-4269-98a4-672e11226c44', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('09b4871b-570f-445d-aca7-cbcd2c2554fa', 'helpManual', '/images', 'Master_Gen_14.png', '40832a73-4b01-4aff-b486-a63fc831d3fe', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('2b375711-52d1-4227-8bab-0a71f2ad0a06', 'helpManual', '/images', 'Master_Gen_15.png', '487defb8-147a-4332-8b22-17f9a3a3efba', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('8c0c1071-6304-4937-8b04-25a9ab708809', 'helpManual', '/images', 'Master_Gen_16.png', '8bf20804-e7bb-451c-a5d9-79e29e15b1c7', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('546a81e2-8f14-468c-8f9c-69217cdc8567', 'helpManual', '/images', 'Master_Gen_17.png', '096af89f-d6f0-4d3a-8b87-1c600f8065b1', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('e2795bd4-c8a2-4c24-b884-77907843225f', 'helpManual', '/images', 'Master_Gen_18.png', '46791d02-a77b-46d0-8f88-034b8b3245c2', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('d3f2a0d3-f362-4f1f-9f68-8c01c9e0ea26', 'helpManual', '/images', 'Master_Gen_19.png', '6ab13cef-660c-4879-a726-a86d041b3b3b', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('3939ab4c-b91f-448e-ab5e-b02a176f039c', 'helpManual', '/images', 'Master_Gen_20.png', '96b17b0f-0acb-447c-8f94-d7419c72ed63', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('64661577-d84b-423e-bc73-73aeebb6cf3a', 'helpManual', '/images', 'Master_Gen_21.png', '922668dc-a8f9-416f-afc9-fa2f520e4c52', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('2e898ae2-bcf3-4a42-985f-587d682f7e81', 'helpManual', '/images', 'Master_Gen_22.png', '1cca9a67-87af-43a9-92a5-abd3512a7203', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('a0246bf6-0b79-4729-9032-b06f0f4fd6ab', 'helpManual', '/images', 'Master_Gen_23.png', 'cd7211c8-0fef-402d-a746-7dade2967768', 'admin@jquiver.io', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),

('afac4070-6d15-405e-9224-5fd54d17242e', 'helpManual', '/images', 'Template_1.png', '2a031ee3-0665-4a40-8edc-5d0c09a51bf0', 'admin@jquiver.io', NOW(), '17feffba-99f4-4591-9cb5-0fef46ee0b77'), 
('b0bfdd7e-3c31-4974-b22f-e103ab533d19', 'helpManual', '/images', 'Template_2.png', '5c9019c1-b141-4b87-a353-6a29fcb48e95', 'admin@jquiver.io', NOW(), '17feffba-99f4-4591-9cb5-0fef46ee0b77'), 
('ebaf8c2a-3932-48be-9418-616f94f5cebd', 'helpManual', '/images', 'Template_3.png', 'fc4c47c9-a7f1-4cca-a0f4-93858abb5b26', 'admin@jquiver.io', NOW(), '17feffba-99f4-4591-9cb5-0fef46ee0b77'), 
('aa882b04-dc5e-4ec3-a299-e3409195fff3', 'helpManual', '/images', 'Template_4.png', '20c4d388-dfc4-4e03-a7b6-382ae257b708', 'admin@jquiver.io', NOW(), '17feffba-99f4-4591-9cb5-0fef46ee0b77'), 
('5d29a3c4-7013-4cab-bb59-ebb9af9321ec', 'helpManual', '/images', 'Template_5.png', '89f8b1a5-6ad0-47e9-ad1b-895deafeed47', 'admin@jquiver.io', NOW(), '17feffba-99f4-4591-9cb5-0fef46ee0b77'), 
('ef715386-7f92-40a7-b950-c5a169bf3055', 'helpManual', '/images', 'Template_6.png', 'd6a4806e-dd0c-4ac3-8bc6-4285eca3408b', 'admin@jquiver.io', NOW(), '17feffba-99f4-4591-9cb5-0fef46ee0b77'),
('105451da-4d8c-4c3f-aab7-602a356c8613', 'helpManual', '/images', 'Template_7.png', 'e334fe90-fbcf-4765-beef-c5b13a2303fe', 'admin@jquiver.io', NOW(), '17feffba-99f4-4591-9cb5-0fef46ee0b77'),

('70fb1426-4f15-485b-9c7f-d5680367ac44', 'helpManual', '/images', 'FormBuilder_1.png', 'cd58ee81-d74a-4eb6-8d0b-fcd84f4f61a5', 'admin@jquiver.io', NOW(), '6938f0ac-00fe-4b94-95e7-02ef72016fe4'),
('48ae2084-638c-4635-8ff0-e3566eef7076', 'helpManual', '/images', 'FormBuilder_2.png', '9bb13402-68c3-430e-a314-ea9d46b982a4', 'admin@jquiver.io', NOW(), '6938f0ac-00fe-4b94-95e7-02ef72016fe4'),
('e06e6861-ba6c-463e-bb03-fea83ab2ca2d', 'helpManual', '/images', 'FormBuilder_3.png', 'f4eeb982-cd10-4756-ba90-0c05a102f844', 'admin@jquiver.io', NOW(), '6938f0ac-00fe-4b94-95e7-02ef72016fe4'),
('d2b348d2-2917-4759-bd45-cfdda13d40bc', 'helpManual', '/images', 'FormBuilder_4.png', '9fd466b0-1c3a-4060-a221-30ddb2fa6966', 'admin@jquiver.io', NOW(), '6938f0ac-00fe-4b94-95e7-02ef72016fe4'),
('0d534ed2-025d-43b4-bc30-d327b57185c6', 'helpManual', '/images', 'FormBuilder_5.png', '4d695d22-078a-4856-abe4-1c1f39a3994a', 'admin@jquiver.io', NOW(), '6938f0ac-00fe-4b94-95e7-02ef72016fe4'),
('42bab23c-c1b6-4db6-a9e5-370fa9877dd7', 'helpManual', '/images', 'FormBuilder_6.png', 'd5f3c731-b3ad-4a0e-8c3a-9adf180f9105', 'admin@jquiver.io', NOW(), '6938f0ac-00fe-4b94-95e7-02ef72016fe4'),
('5cf017d5-e9c1-4bbb-bc8c-8f0a303c4aab', 'helpManual', '/images', 'FormBuilder_7.png', 'd896c7e2-0665-42a9-8829-71d81b65626d', 'admin@jquiver.io', NOW(), '6938f0ac-00fe-4b94-95e7-02ef72016fe4'),
('61c42c44-c8c4-4f91-b82a-1a6f0d3bf6b3', 'helpManual', '/images', 'FormBuilder_8.png', 'beb735bf-386e-4cd3-8f36-843b8f841556', 'admin@jquiver.io', NOW(), '6938f0ac-00fe-4b94-95e7-02ef72016fe4'),
('efe2818e-85a0-47c1-b4e9-161bbbceeb11', 'helpManual', '/images', 'FormBuilder_9.png', '9a3c2b1f-5c38-453a-9e6f-ad26c3d926f1', 'admin@jquiver.io', NOW(), '6938f0ac-00fe-4b94-95e7-02ef72016fe4'),
('c1cbfbf9-d4d2-42aa-b016-8f78648d6702', 'helpManual', '/images', 'FormBuilder_10.png', '54c2771c-15f7-4a3e-858b-85b260a2ee0e', 'admin@jquiver.io', NOW(), '6938f0ac-00fe-4b94-95e7-02ef72016fe4'),
('d94b3518-d835-4265-8699-aa3abc2583c3', 'helpManual', '/images', 'FormBuilder_11.png', 'eaa93ff6-da72-46e7-85e3-ddb04fbc5948', 'admin@jquiver.io', NOW(), '6938f0ac-00fe-4b94-95e7-02ef72016fe4'),
('1b9ecc49-2ca8-4fa0-a686-b9aa0990c888', 'helpManual', '/images', 'FormBuilder_12.png', '29f638bc-b776-48e5-ac7e-3e19b0455e05', 'admin@jquiver.io', NOW(), '6938f0ac-00fe-4b94-95e7-02ef72016fe4'),
('6ed4d9dd-b837-46d8-a03a-3720432861b9', 'helpManual', '/images', 'FormBuilder_13.png', '97b75d53-1953-4be0-a39a-1e21207fa9b7', 'admin@jquiver.io', NOW(), '6938f0ac-00fe-4b94-95e7-02ef72016fe4'),
('3b358b1b-bc49-4ef9-ae75-dfc9f6452dbe', 'helpManual', '/images', 'FormBuilder_14.png', '6e03ed9e-c334-4f83-bf81-24e3d1d12b5b', 'admin@jquiver.io', NOW(), '6938f0ac-00fe-4b94-95e7-02ef72016fe4'),
('27f8447b-4e43-4715-ae48-e3b972c05942', 'helpManual', '/images', 'FormBuilder_15.png', '62032346-51f5-446a-b4ac-d8115c2cfd38', 'admin@jquiver.io', NOW(), '6938f0ac-00fe-4b94-95e7-02ef72016fe4'),

('bf81338a-f15e-4cf7-97af-df7faa22b56b', 'helpManual', '/images', 'AutoComplete_1.png', '3cee67a9-1876-4929-8f15-46b0000fcb6e', 'admin@jquiver.io', NOW(), '5e46df00-e07a-4b73-889f-2894adfd3df8'),
('7643a091-d2c5-498e-8c20-33dc2952da0b', 'helpManual', '/images', 'AutoComplete_2.png', 'cda1b460-94fd-4188-9f17-e59a3411f3d7', 'admin@jquiver.io', NOW(), '5e46df00-e07a-4b73-889f-2894adfd3df8'),
('76c6d5b3-b55d-46cb-9ecf-6b101317fbbb', 'helpManual', '/images', 'AutoComplete_3.png', '8eca0b62-ba3d-42e3-bcdd-0dd969b197f6', 'admin@jquiver.io', NOW(), '5e46df00-e07a-4b73-889f-2894adfd3df8'),
('83d19871-84a3-479b-9a1c-293580538308', 'helpManual', '/images', 'AutoComplete_4.png', 'e8adb137-873d-400c-a8f9-ab070d3d3fb7', 'admin@jquiver.io', NOW(), '5e46df00-e07a-4b73-889f-2894adfd3df8'),
('8fd8f497-9fee-49f8-b785-5aea3fa8252a', 'helpManual', '/images', 'AutoComplete_5.png', 'a5c15b3e-e807-4a5c-8faa-2c94f7d75500', 'admin@jquiver.io', NOW(), '5e46df00-e07a-4b73-889f-2894adfd3df8'),
('5083f06b-6d49-4eea-952c-0b7ef0373094', 'helpManual', '/images', 'AutoComplete_6.png', '83d4ce63-da36-4dcd-8f50-50d28b943c29', 'admin@jquiver.io', NOW(), '5e46df00-e07a-4b73-889f-2894adfd3df8'),
('3ecffe0c-1acb-44db-a179-bb59552d0244', 'helpManual', '/images', 'AutoComplete_7.png', '0962284d-4b3b-4973-95cc-b4608b54f682', 'admin@jquiver.io', NOW(), '5e46df00-e07a-4b73-889f-2894adfd3df8'),
('3e7c18be-2963-41b0-93fd-b5ebab0c24f7', 'helpManual', '/images', 'AutoComplete_8.png', '079c44cf-dd9f-47a5-b33a-7dcb089f434c', 'admin@jquiver.io', NOW(), '5e46df00-e07a-4b73-889f-2894adfd3df8'),
('22bb3522-f5ed-4609-a506-eca9172f78a6', 'helpManual', '/images', 'AutoComplete_9.png', '110838bb-d67e-4ade-a93a-fbbe735eacda', 'admin@jquiver.io', NOW(), '5e46df00-e07a-4b73-889f-2894adfd3df8'),
('6bab60b9-59ea-4b2b-9e3e-8363bc9fcfef', 'helpManual', '/images', 'AutoComplete_10.png', 'c1c355a0-9c24-4cf8-b9af-a9b13c392e5e', 'admin@jquiver.io', NOW(), '5e46df00-e07a-4b73-889f-2894adfd3df8'),
('912c7fd3-5de4-4d65-b0a8-c01bbb2c81fc', 'helpManual', '/images', 'AutoComplete_11.png', 'c6094d0d-c92c-48bd-85fc-2c0e76679ebd', 'admin@jquiver.io', NOW(), '5e46df00-e07a-4b73-889f-2894adfd3df8'),
('7fe47d81-7327-40b7-943b-0a8380999ac2', 'helpManual', '/images', 'AutoComplete_12.png', 'f1cd3c84-7877-483c-9fea-70ec16391b1a', 'admin@jquiver.io', NOW(), '5e46df00-e07a-4b73-889f-2894adfd3df8'),

('6dab2b82-c56b-4b75-8363-744746ec7ec0', 'helpManual', '/images', 'Property_Master_1.png', 'dedfac91-60a9-4767-9d3e-5312f587e7d0', 'admin@jquiver.io', NOW(), '918676c8-b653-43ee-964a-d4faaeb13787'),
('f572d5d4-61ab-4dbd-9b87-1b73b24041c2', 'helpManual', '/images', 'Property_Master_2.png', '8a15ba46-6e7b-48b5-b196-863397a9d678', 'admin@jquiver.io', NOW(), '918676c8-b653-43ee-964a-d4faaeb13787'),
('57b0a742-8e3a-48a1-b8d3-487c91725ac5', 'helpManual', '/images', 'Property_Master_3.png', 'c5ecb891-6cfc-43ff-a6bd-bb27c8ad9fb9', 'admin@jquiver.io', NOW(), '918676c8-b653-43ee-964a-d4faaeb13787'),
('f4dee366-0621-41f2-aed2-33f81ab280c2', 'helpManual', '/images', 'Property_Master_4.png', 'b36fc9f0-df7e-4cee-8677-a06963e7442c', 'admin@jquiver.io', NOW(), '918676c8-b653-43ee-964a-d4faaeb13787'),
('b5fab228-0db4-407a-9f50-c28a45c703af', 'helpManual', '/images', 'Property_Master_5.png', '5208bb32-eeb1-4e91-b067-371ee7c3be65', 'admin@jquiver.io', NOW(), '918676c8-b653-43ee-964a-d4faaeb13787'),
('c573a8da-4e0b-41f9-956c-186c123721ea', 'helpManual', '/images', 'Property_Master_6.png', '76e08c78-a2d6-4d6a-b210-a9be606eab5d', 'admin@jquiver.io', NOW(), '918676c8-b653-43ee-964a-d4faaeb13787'),
('22f806b9-3ab1-45db-90de-575651d64e0a', 'helpManual', '/images', 'Property_Master_7.png', '1384b127-3f71-4e2c-9d2a-eaa7db20f63f', 'admin@jquiver.io', NOW(), '918676c8-b653-43ee-964a-d4faaeb13787'),
('4abc8a9f-bf90-4008-a517-30c06ec6e4e6', 'helpManual', '/images', 'Property_Master_8.png', '52cbbb1c-078b-4b7d-9967-c7147485c036', 'admin@jquiver.io', NOW(), '918676c8-b653-43ee-964a-d4faaeb13787'),
('37bb5551-34fb-417f-a7f4-c59a6645ae00', 'helpManual', '/images', 'Property_Master_9.png', 'ee03fdaf-1329-4d8b-a94a-79da7d4ccce4', 'admin@jquiver.io', NOW(), '918676c8-b653-43ee-964a-d4faaeb13787'),
('a410b60d-4687-4c18-b7fa-9e744acfffaa', 'helpManual', '/images', 'Property_Master_10.png', '85491459-29bd-4ae2-a837-0e57328a0231', 'admin@jquiver.io', NOW(), '918676c8-b653-43ee-964a-d4faaeb13787'),
('f072bd74-00fd-4a56-b928-c71f622a0287', 'helpManual', '/images', 'Property_Master_11.png', '635a3d78-987f-484b-922d-994fb9fe33b9', 'admin@jquiver.io', NOW(), '918676c8-b653-43ee-964a-d4faaeb13787'),
('36675e75-029e-4d46-966f-f5060ae3099a', 'helpManual', '/images', 'Property_Master_12.png', '3efb0a89-de0c-4898-8634-8cb3e0b765ef', 'admin@jquiver.io', NOW(), '918676c8-b653-43ee-964a-d4faaeb13787'),
('5a852acb-d095-44e5-af60-62f558a8c1d3', 'helpManual', '/images', 'Property_Master_13.png', '845754b7-d840-4059-8053-a11060cde98c', 'admin@jquiver.io', NOW(), '918676c8-b653-43ee-964a-d4faaeb13787'),
('fc650462-72af-4a16-a074-2829d6d1c84f', 'helpManual', '/images', 'Property_Master_14.png', '7b915406-235e-4906-9a63-7bb6d5ded880', 'admin@jquiver.io', NOW(), '918676c8-b653-43ee-964a-d4faaeb13787'),
('740c4dd8-4e35-4691-bc53-da82942b1e0b', 'helpManual', '/images', 'Property_Master_15.png', '0b701e3a-062b-47d3-b503-2a5ad3e08f58', 'admin@jquiver.io', NOW(), '918676c8-b653-43ee-964a-d4faaeb13787'),

('1e6fc271-65d7-4f92-840e-4c070192081a', 'helpManual', '/images', 'User_Management_1.png', 'cebc2b56-66c3-4014-9194-11d3e2d22632', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('03abf3a9-f621-4b52-89c2-c0532b7e378f', 'helpManual', '/images', 'User_Management_2.png', 'dc7abca6-eb54-4df3-8174-be7b5670aa05', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('60d7a5b6-2338-4142-95e0-b3bcd79c7944', 'helpManual', '/images', 'User_Management_3.png', 'e74338ce-8bbd-4cd0-a894-a8343bf62016', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('8b2d5d39-8610-4e5d-b267-23248793530a', 'helpManual', '/images', 'User_Management_4.png', 'a6da3700-1763-435a-bbaa-12f3760f30e1', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('2daa37b8-f09d-4ab3-ba44-8efbd2528a72', 'helpManual', '/images', 'User_Management_5.png', '0c0ce85d-ff85-4b01-9093-ef29da99bffd', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('662d815b-07e4-4507-a17e-3d46f8bb09c7', 'helpManual', '/images', 'User_Management_6.png', '2d2648d2-4589-491b-95aa-49d0bc8a5a26', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('d68df2d8-2b39-4ffd-b25c-ab1d6310ce53', 'helpManual', '/images', 'User_Management_7.png', 'f7ef0100-1f5d-4162-bd4d-ab85f4d53ca4', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('ed8eeba0-7381-4d01-bd8d-6d13165e6f02', 'helpManual', '/images', 'User_Management_8.png', 'd14a93b7-ff21-40ac-b3a7-59050189a4f9', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('a56d471a-2aa0-4355-a166-aff429db4d3c', 'helpManual', '/images', 'User_Management_9.png', 'df042038-1b69-45a8-855d-bd56311bd2b0', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('4081d3ef-bd32-43f4-9fa1-be7f794b21e2', 'helpManual', '/images', 'User_Management_10.png', 'c2a0ddf7-4c57-45fc-85b3-b0f8c09a7b7c', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('3323e9a9-075e-41f5-a1b6-0bce405da169', 'helpManual', '/images', 'User_Management_11.png', '83181b67-b708-4259-b0c8-b8d0ce39dd77', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('40232f8b-c302-4d82-b155-c92b21600022', 'helpManual', '/images', 'User_Management_12.png', '229aca79-9d34-41df-b162-455987c662e4', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('53f1de82-74df-4421-8dd1-ab22f4b6e866', 'helpManual', '/images', 'User_Management_13.png', '18b86c35-ed85-410c-805f-7b5445365156', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('501b9200-a556-436c-8474-33c14b3c1593', 'helpManual', '/images', 'User_Management_14.png', 'a9ced256-fd9d-4856-b2bc-3d6bf172f6fb', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('f2c32ac3-eb0f-46c9-9007-392da106c74f', 'helpManual', '/images', 'User_Management_15.png', '9f2b3a9d-949a-416d-beb8-3af700b8c4e9', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('d512723b-5234-48c6-8981-68986e3dbbd1', 'helpManual', '/images', 'User_Management_28.png', '9b816e22-4371-4aee-8863-ee7b09d9f00f', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('2e424b52-74fd-478a-a9de-54fa5ef899f1', 'helpManual', '/images', 'User_Management_16.png', 'a88a697c-d228-4b5b-9f6b-7fe12e21ee60', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('d3dcb795-33a3-4dba-a457-be1afc3cde8f', 'helpManual', '/images', 'User_Management_18.png', '8a38ad53-1941-49ae-8965-c7a42d2794a5', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('9c7b44b9-22b6-416b-9781-b0f97b48a45e', 'helpManual', '/images', 'User_Management_19.png', '93e22094-3b0b-4ba3-a943-0b97463c25bb', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('d1964d46-c60d-4f92-9d27-db0d234fe243', 'helpManual', '/images', 'User_Management_20.png', '51f1c7c8-6e5b-43f9-9e34-61e80914d9c4', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('90b3dd04-3fcd-4172-be7e-265658f8256f', 'helpManual', '/images', 'User_Management_21.png', '7aa8dc58-ae31-4af7-8f99-907feab4e09b', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('062399fe-986c-4ce4-b1c2-17a57c2a0953', 'helpManual', '/images', 'User_Management_22.png', '3fc3e1ed-bb5c-47b0-8b7f-feb309a1e420', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('f072cbdd-5ec6-46cc-b25a-567a3bee20e3', 'helpManual', '/images', 'User_Management_23.png', '9a9b4218-ce8a-4597-83b7-bb31bc773bef', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('bdbb2a8b-308e-4dfc-8914-195511acac19', 'helpManual', '/images', 'User_Management_24.png', '54d84711-8724-46d9-bb6a-3588b6cf73ee', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('72b2d24b-9adf-4ddc-ac3b-215ef2921c28', 'helpManual', '/images', 'User_Management_25.png', 'eb7c49ff-fbe3-40cb-808f-a31ff8f1c16f', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('2b2e833c-865e-484d-bbe8-5dc977a961cc', 'helpManual', '/images', 'User_Management_27.png', 'dd78e16e-6927-4131-8485-6b9298448498', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('eafca097-b1f7-46c8-8ba9-9b8b9d4ea806', 'helpManual', '/images', 'User_Management_30.png', '5bd60d41-df08-43a9-a2a4-6b1f4aed492c', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('405cf635-0629-4cd2-a555-f37c0f969a10', 'helpManual', '/images', 'User_Management_31.png', 'ed78f128-d5c0-4c81-8f13-2e0be77c5a46', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('dd858d3a-dc56-407c-9359-99a7c43c9f3a', 'helpManual', '/images', 'User_Management_32.png', '1fe81e57-c753-4e3c-938a-93de8497f696', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('91f5ad0e-5ea5-4df1-a50a-2571540d7e3d', 'helpManual', '/images', 'User_Management_33.png', 'c0cdc842-d0cf-4baf-8155-afd249bab130', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('57a259c3-7458-4f5d-88e6-a9157409abcc', 'helpManual', '/images', 'User_Management_34.png', 'b511a645-ccbf-4366-a2d9-704beadd56ea', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('9ad4cb86-db50-4bd9-b5d7-592cf9778714', 'helpManual', '/images', 'User_Management_35.png', '7161823e-0c0a-495c-bf01-5c1ccc2fb7dd', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('855c65ef-0749-4fdd-ad3e-8ece7381e80d', 'helpManual', '/images', 'User_Management_36.png', '9f2b1bbe-fb18-4376-937a-674248247930', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('2b00768c-6d3b-4e21-9c38-e6764de990a4', 'helpManual', '/images', 'User_Management_37.png', '7578c802-97bc-49df-b118-969c1242fa21', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('d5d4098a-3ac8-4bfd-a53a-bddeca3be413', 'helpManual', '/images', 'User_Management_38.png', '45f2e455-b00d-4b93-ad4b-7a6f2153ecd8', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('71b5a6ba-0ba2-490f-844a-e609463992ab', 'helpManual', '/images', 'User_Management_39.png', 'e3a567ed-e742-4ae4-b519-de4dc0cf7f20', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('68812ef5-9384-4f6c-be0f-b814d12cd5fb', 'helpManual', '/images', 'User_Management_40.png', '2d40c36f-5fd0-4ae6-9946-ac5d4bd512c0', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('bbee2a7a-99ef-42c9-9f23-53bad117fab8', 'helpManual', '/images', 'User_Management_41.png', 'ed3f4526-509a-4ff8-aa9a-67cb23fd7e0c', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('d2d8ee5b-ccf6-4540-aceb-257461346cba', 'helpManual', '/images', 'User_Management_42.png', 'd997a6ba-a9c3-448a-9dff-df738a82731d', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('2ddbb6f7-a479-4aa3-9035-16d101567631', 'helpManual', '/images', 'User_Management_43.png', '14d4e6b1-074c-4c8a-a0b3-8be7ba778608', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('60afcd54-19b2-44d8-880b-c2407b168c40', 'helpManual', '/images', 'User_Management_44.png', '12785995-1660-4be8-93e2-4bdecc918933', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('7c5f5b84-d4bb-4e6a-99e0-571701486dc8', 'helpManual', '/images', 'User_Management_45.png', 'ff9f2556-d50e-4678-b09b-74d1e40993f9', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('467f02a8-e2b5-49a6-a441-b99280887972', 'helpManual', '/images', 'User_Management_46.png', '84a613ac-af5e-477a-ae72-ae3f41cac369', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('795650d6-1c12-4b45-a03d-75f897198e31', 'helpManual', '/images', 'User_Management_47.png', '08e1c1ea-71bf-43f6-a4bb-fd295cddfbcd', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('b05fa373-b29a-43a1-ae2f-3601ba5966d7', 'helpManual', '/images', 'User_Management_48.png', '01923014-2cb8-464e-b0ce-f4530b1ba3a3', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('c8e0d007-d7e7-40db-944b-a444975d581c', 'helpManual', '/images', 'User_Management_49.png', 'bc93540a-fefc-48c0-b06f-546fafb19750', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('3a2ff958-a2da-4acb-9523-180591f3d69b', 'helpManual', '/images', 'User_Management_51.png', '1ae73de8-3182-47b1-ae99-978e68e5942e', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('f6a6d95f-c5c4-4bf2-b21e-7e7edac001ce', 'helpManual', '/images', 'User_Management_52.png', 'b60348c3-cd6b-455e-96a8-a12d85e2c48d', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('9901102a-2c43-49ca-bd91-6143f166c2a4', 'helpManual', '/images', 'User_Management_53.png', 'b82a34cb-0f32-4360-a802-2be33c358c95', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('d6c6ae56-b832-41ff-94a3-6b3b08bf890c', 'helpManual', '/images', 'User_Management_54.png', '7dda923c-e128-4a10-b981-3961d30fd2d6', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('a1a365ef-cb6c-41bb-835e-31ea2b9f74c4', 'helpManual', '/images', 'User_Management_55.png', 'cccdf103-953c-4100-a4b1-52318ca37730', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('858fce27-019e-4844-8225-1ec9cb3ca94f', 'helpManual', '/images', 'User_Management_56.png', '072500bf-3db9-4008-a482-46f7d8fb0e1f', 'admin@jquiver.io', NOW(), 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),

('d92d7c34-4d04-4ae4-b70d-5db94deeb1b4', 'helpManual', '/images', 'FileBin_1.png', 'fe3bfb0d-fd74-4610-9d08-39cea9313784', 'admin@jquiver.io', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'),
('d3580ccd-8f4e-4ca1-b824-03fe6acad44f', 'helpManual', '/images', 'FileBin_2.png', '04361037-cc3b-4720-8ee4-281b68ba3298', 'admin@jquiver.io', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'),
('c70f4b62-d920-4624-a31b-b818eb7f1544', 'helpManual', '/images', 'FileBin_3.png', '801fed2d-6bc9-4ec9-83ae-2e25de2b2e52', 'admin@jquiver.io', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'),
('3043e1da-ace2-41e0-80a9-c00bfc5e4f18', 'helpManual', '/images', 'FileBin_4.png', 'c171d983-c347-4cf1-b379-5e068e1f04b3', 'admin@jquiver.io', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'),
('88434124-6d3a-4330-8c57-1bda0e623e7f', 'helpManual', '/images', 'FileBin_5.png', '79917548-843a-47c4-8899-40623cf4a981', 'admin@jquiver.io', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'),
('0c5893cc-8fe2-478c-8262-e11399c73e3b', 'helpManual', '/images', 'FileBin_6.png', '8fe3a612-31b4-4a3a-97b0-260f31936417', 'admin@jquiver.io', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'),
('38722876-59a2-4f23-beca-6812444a2aaf', 'helpManual', '/images', 'FileBin_7.png', '0dee1f9e-915f-4f99-ab08-589ec1c3aedd', 'admin@jquiver.io', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'),
('843ab443-b45b-457b-9eb5-b539af54bf87', 'helpManual', '/images', 'FileBin_8.png', 'f49a58d8-5554-42e0-8978-668d3554ce68', 'admin@jquiver.io', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'),
('fe297477-dd0a-4647-8844-dc65c2356d3b', 'helpManual', '/images', 'FileBin_9.png', '7f175453-ae47-4d1d-913d-6a67fceebf44', 'admin@jquiver.io', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'),
('39cbb1c3-7472-4e12-9748-6400bf7a2fec', 'helpManual', '/images', 'FileBin_10.png', '1a641d12-ac7e-49b1-aa62-753910afb1f9', 'admin@jquiver.io', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'),
('28c51aa6-a39c-492d-aff2-123008fafbe1', 'helpManual', '/images', 'FileBin_11.png', '9796c648-835a-467c-8c60-ce7fff10140d', 'admin@jquiver.io', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'),
('80c5f8a5-23db-43e9-94b4-a75853b05dbe', 'helpManual', '/images', 'FileBin_12.png', '5fcf32c4-dcef-49df-8303-6d97f602bdfb', 'admin@jquiver.io', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'),
('bf72927a-0a50-466b-b284-0854bc012e85', 'helpManual', '/images', 'FileBin_13.png', '5598c538-db3d-46f2-9467-bbd57e22a6b4', 'admin@jquiver.io', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'),
('28e77767-8fad-4d46-a126-666413aed318', 'helpManual', '/images', 'FileBin_14.png', '9116c63b-2ab8-4720-b006-50ae9665c70b', 'admin@jquiver.io', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'),
('a2da1bf7-67cf-4ec0-96ef-d89604f61003', 'helpManual', '/images', 'FileBin_15.png', '9f63ebea-4c73-4a0a-a877-20aa7a93844a', 'admin@jquiver.io', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'),
('f1968fb9-559b-47fd-bdc7-07e6b3453263', 'helpManual', '/images', 'FileBin_16.png', 'b201f1de-0b52-43ab-ae55-f948ff386e97', 'admin@jquiver.io', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'),
('69b75a97-fe0b-460e-ab0d-116821a30727', 'helpManual', '/images', 'FileBin_17.png', 'c7eabc5c-900f-4b40-8987-e7703532f308', 'admin@jquiver.io', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'),
('aeda35e3-bd33-4485-bba8-c43c35693f51', 'helpManual', '/images', 'FileBin_18.png', '0879e8e6-fed7-4f25-9c33-904ba79cd614', 'admin@jquiver.io', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'),

('47c77f23-d06b-4ff2-9991-d60db2ae9fd0', 'helpManual', '/images', 'Dashboard_1.png', '6dcd5999-4355-479f-8163-7ecba8f78fb9', 'admin@jquiver.io', NOW(), '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'),
('fd119b35-a6fe-476d-9561-46b2a7c90341', 'helpManual', '/images', 'Dashboard_2.png', '8c7a1426-6169-4535-8f18-7b95b81dd3e8', 'admin@jquiver.io', NOW(), '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'),
('51931155-0c10-43e9-bd3d-4bde6aee0431', 'helpManual', '/images', 'Dashboard_3.png', '74bcb93b-c052-4b42-85ee-454c48b43ae2', 'admin@jquiver.io', NOW(), '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'),
('b19e9cbb-b05a-4e29-a398-0feff561a0e4', 'helpManual', '/images', 'Dashboard_4.png', '81dfa7e1-1f41-4968-bf63-20e9be66c42b', 'admin@jquiver.io', NOW(), '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'),
('8e530a09-c3b1-4197-a49e-3d220f4e063e', 'helpManual', '/images', 'Dashboard_6.png', '8511ea80-619c-4e9c-89dd-2652a312f041', 'admin@jquiver.io', NOW(), '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'),
('fc0c2781-92fe-4879-8dab-ce0a893b40b8', 'helpManual', '/images', 'Dashboard_7.png', 'be741375-c97d-468f-87f4-107a3aa2dd18', 'admin@jquiver.io', NOW(), '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'),
('3b7ea334-d513-4827-bd5a-4ff88f08d571', 'helpManual', '/images', 'Dashboard_8.png', '74b4d572-7cf0-4a44-be50-82f73dd86a7a', 'admin@jquiver.io', NOW(), '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'),
('cbeb00be-4b05-4d77-89b6-8242b85eb3ac', 'helpManual', '/images', 'Dashboard_9.png', '525ec89e-bd72-4463-87a6-d8814a5516d0', 'admin@jquiver.io', NOW(), '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'),
('0898958c-8cce-4ac3-8f6f-1ce5b886acfe', 'helpManual', '/images', 'Dashboard_10.png', 'b7dfe4a8-76e8-485a-9208-9fc9237328ec', 'admin@jquiver.io', NOW(), '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'),
('45ab6497-5953-4120-b05b-ea9365ebe648', 'helpManual', '/images', 'Dashboard_11.png', 'f8da2c52-135b-4d49-9b51-31e4fee0ea41', 'admin@jquiver.io', NOW(), '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'),
('60905a88-b892-4b9b-9e9c-0b4682ebef9a', 'helpManual', '/images', 'Dashboard_12.png', '189b95b1-552c-4cae-b70d-0792c5664753', 'admin@jquiver.io', NOW(), '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'),
('f6088a79-2a9a-428f-bf41-955bf8b72f19', 'helpManual', '/images', 'Dashboard_13.png', '2899e367-9c5c-4901-a8af-b02c91fc0428', 'admin@jquiver.io', NOW(), '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'),
('65fc7e48-cda1-4661-8437-dea3533c4a54', 'helpManual', '/images', 'Dashboard_14.png', 'b2cd32f1-c5fb-4be2-addb-21e9b08b478c', 'admin@jquiver.io', NOW(), '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'),
('588f22d8-ea20-4345-b825-e564444a8322', 'helpManual', '/images', 'Dashboard_15.png', '75ea5fe2-e383-400a-8a16-da644cfc78ee', 'admin@jquiver.io', NOW(), '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'),
('8fce9b14-8a4e-491e-b8e0-100462515b40', 'helpManual', '/images', 'Dashboard_16.png', '2327409a-7e31-4808-ab42-e450088142b0', 'admin@jquiver.io', NOW(), '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'),
('75db071a-43e2-4578-b0a4-38c919a3bca6', 'helpManual', '/images', 'Dashboard_17.png', '4a9d470d-d3e1-4600-b870-87931eeb6b5c', 'admin@jquiver.io', NOW(), '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'),
('8e66f0dd-5d85-4b93-b8a6-e045f19435a7', 'helpManual', '/images', 'Dashboard_18.png', '2ab4456e-3ffc-4a8b-8eff-6e28ac87a054', 'admin@jquiver.io', NOW(), '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'),
('197e8754-a4d2-4265-a322-cbed789324c0', 'helpManual', '/images', 'Dashboard_19.png', '1d0c9f97-b3f6-4cfb-b2b7-e6eba7339bc7', 'admin@jquiver.io', NOW(), '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'),
('f71f5cfb-0457-4b62-9177-5d223ff95aaa', 'helpManual', '/images', 'Dashboard_20.png', '691108c0-4eab-47c7-988c-34bfb62a6c04', 'admin@jquiver.io', NOW(), '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'),
('58c9e26e-c4ca-4eb4-90e2-a4d740545c5a', 'helpManual', '/images', 'Dashboard_21.png', '7fbb490b-b6ed-466e-8b96-829c53442486', 'admin@jquiver.io', NOW(), '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'),
('7d339fde-6e6f-47c3-bd25-46b98eaa96bb', 'helpManual', '/images', 'Dashboard_22.png', '3c43db18-6303-48ca-8cde-b79b2420303a', 'admin@jquiver.io', NOW(), '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'),
('32494c32-ddcf-4d77-b621-c1fdcc7449ad', 'helpManual', '/images', 'Dashboard_23.png', '3f4d161d-9cf1-4bf6-bb7d-ce661f6fc66a', 'admin@jquiver.io', NOW(), '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'),
('3a946afd-e5c8-477a-bbf8-89f7e4ea6099', 'helpManual', '/images', 'Dashboard_24.png', '2fb20879-349d-4431-9510-10a79059147b', 'admin@jquiver.io', NOW(), '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'),

('c1052fca-406c-473d-8d26-19a6cb1641e1', 'helpManual', '/images', 'Versioning_1.png', 'a343c134-4a9b-4ea5-9304-19548100598b', 'admin@jquiver.io', NOW(), '1d5577fa-a7e3-4bdf-85ed-b84ac81fbb0b'),
('20407136-778d-4237-a355-dcdbefc74703', 'helpManual', '/images', 'Versioning_2.png', '24f56e36-dd13-45c9-aa47-b390721297bb', 'admin@jquiver.io', NOW(), '1d5577fa-a7e3-4bdf-85ed-b84ac81fbb0b'),
('a15256c3-7bec-4577-9c15-f9d8668ca68f', 'helpManual', '/images', 'Versioning_3.png', '60471fd6-e408-48ff-9d1e-13cb7731ed43', 'admin@jquiver.io', NOW(), '1d5577fa-a7e3-4bdf-85ed-b84ac81fbb0b'),
('aa99668a-7aba-4d09-8c04-af127289db1f', 'helpManual', '/images', 'Versioning_4.png', 'b7c535ad-c511-431e-9fc1-58b3429a0768', 'admin@jquiver.io', NOW(), '1d5577fa-a7e3-4bdf-85ed-b84ac81fbb0b'),
('8961a88b-be30-452e-9ae7-314edf76e340', 'helpManual', '/images', 'Versioning_5.png', '575b5652-7915-45be-ae01-7d38fc7ddf0c', 'admin@jquiver.io', NOW(), '1d5577fa-a7e3-4bdf-85ed-b84ac81fbb0b'),
('915c16d4-7de6-4026-87ee-4ab39b2b4c19', 'helpManual', '/images', 'Versioning_6.png', 'a264dcb4-e6a0-4bdd-a74a-0687186a9b00', 'admin@jquiver.io', NOW(), '1d5577fa-a7e3-4bdf-85ed-b84ac81fbb0b'),
('1e44e825-7d8d-4485-8e59-20a74888106b', 'helpManual', '/images', 'Versioning_7.png', '3a2c55c3-4453-4798-bde9-9a55f2db99f6', 'admin@jquiver.io', NOW(), '1d5577fa-a7e3-4bdf-85ed-b84ac81fbb0b'),
('1c012d8f-750d-4af1-aa07-0dab31cd50ac', 'helpManual', '/images', 'Versioning_8.png', 'f021d1f3-b0d5-4f2b-9fe6-d891d7ec7abd', 'admin@jquiver.io', NOW(), '1d5577fa-a7e3-4bdf-85ed-b84ac81fbb0b'),
('0c402fca-cf3a-41dd-8693-56156e5cab67', 'helpManual', '/images', 'Versioning_9.png', '6ca0c390-d8b9-4845-947f-f28d64002ca6', 'admin@jquiver.io', NOW(), '1d5577fa-a7e3-4bdf-85ed-b84ac81fbb0b'),

('7a1e3f09-d3dd-4e39-bdf2-3d5003e551be', 'helpManual', '/images', 'Import_1.png', 'd71b3a4e-6473-4998-ae88-c413a264eede', 'admin@jquiver.io', NOW(), 'be37c240-2607-4d79-9ef1-136dbd7c524b'),
('8a277a0a-aa06-45db-aa3d-44a8cbbafa25', 'helpManual', '/images', 'Import_2.png', '4b85a5a8-9ad5-4555-a3b6-1d5c1a746005', 'admin@jquiver.io', NOW(), 'be37c240-2607-4d79-9ef1-136dbd7c524b'),
('8f8c8daf-8538-4168-ad71-e31ac523d8ef', 'helpManual', '/images', 'ImportExport_6.png', '6a681bd5-7cbb-4889-975f-bc0d3b8588e3', 'admin@jquiver.io', NOW(), 'be37c240-2607-4d79-9ef1-136dbd7c524b'),
('ae754fd5-89ac-48a1-8248-5b62aba77384', 'helpManual', '/images', 'ImportExport_7.png', 'c8d310c5-fd6a-4859-b902-955fa9f6b451', 'admin@jquiver.io', NOW(), 'be37c240-2607-4d79-9ef1-136dbd7c524b'),

('f068fa05-6f66-4272-aeb1-f206bbee7bef', 'helpManual', '/images', 'Export_1.png', '539f9122-9ae5-46ad-9e0c-85a8a6818c1c', 'admin@jquiver.io', NOW(), 'dd97c23d-feef-4cea-afcf-3cece7819159'),
('a7097cba-a7c5-4f69-a514-30336afe5e19', 'helpManual', '/images', 'Export_2.png', '09d01396-80a6-4734-8b41-87cd2e23cd54', 'admin@jquiver.io', NOW(), 'dd97c23d-feef-4cea-afcf-3cece7819159'),
('8ba2ceea-21fb-4810-a6f3-c546cabf6f50', 'helpManual', '/images', 'Export_3.png', '1fad5e42-ed8b-43e2-8669-801c619f7de1', 'admin@jquiver.io', NOW(), 'dd97c23d-feef-4cea-afcf-3cece7819159'),
('9189c80b-0151-4d87-9eed-0911ff2c6694', 'helpManual', '/images', 'Export_4.png', 'f981a542-3047-41a8-afb6-2e9f15dfac83', 'admin@jquiver.io', NOW(), 'dd97c23d-feef-4cea-afcf-3cece7819159'),
('0348a0e1-ac68-49eb-a0f5-db241e512028', 'helpManual', '/images', 'Export_5.png', 'edfcb322-e481-4d98-b608-f7bab2670b4d', 'admin@jquiver.io', NOW(), 'dd97c23d-feef-4cea-afcf-3cece7819159'),
('f870d845-5f40-4ee0-b20e-585eea560045', 'helpManual', '/images', 'Export_6.png', '84cd238f-55d6-4564-b133-f3a970912dd0', 'admin@jquiver.io', NOW(), 'dd97c23d-feef-4cea-afcf-3cece7819159'),
('13685dc4-5866-4f91-a927-95e28b39c3e3', 'helpManual', '/images', 'Export_7.png', 'ef880838-5d0a-4df6-9f06-967ef2bb77de', 'admin@jquiver.io', NOW(), 'dd97c23d-feef-4cea-afcf-3cece7819159'),

('5332fa07-f42e-4b40-ad30-bb0ffbad979b', 'helpManual', '/images', 'Dev_Mode_1.png', '59a28e3c-581a-48ac-9088-b097d455140f', 'admin@jquiver.io', NOW(), '935b9394-c33d-4113-a248-27c46c45e7e9'),
('a8956dc5-1b52-4a6f-b326-2c757dc7e850', 'helpManual', '/images', 'Dev_Mode_Template_Storage_1.png', '36817e94-016d-4362-8d90-ee4e58c98933', 'admin@jquiver.io', NOW(), '935b9394-c33d-4113-a248-27c46c45e7e9'),
('d73c1cc1-ec63-49cd-a79c-1891e88de41b', 'helpManual', '/images', 'Dev_Mode_Template_Storage_2.png', '198f0f36-a7b9-4daf-9d17-1aefc166aff3', 'admin@jquiver.io', NOW(), '935b9394-c33d-4113-a248-27c46c45e7e9'),
('58d80ab5-4eae-43da-b61e-d1c5bbd54f86', 'helpManual', '/images', 'Dev_Mode_Temp_1.png', 'f4f845a7-9571-4137-bbbf-b67067948d50', 'admin@jquiver.io', NOW(), '935b9394-c33d-4113-a248-27c46c45e7e9'),
('88950dfd-c8c7-48bf-bbad-6a8a8df67209', 'helpManual', '/images', 'Dev_Mode_UploadForms.png', '79dcb74e-f305-403b-bc06-8cfa80a2f6cd', 'admin@jquiver.io', NOW(), '935b9394-c33d-4113-a248-27c46c45e7e9'),
('5c3bb528-a63e-4ad0-95d1-863d755f3215', 'helpManual', '/images', 'Dev_Mode_Dashlet_1.png', '84eca600-1b16-47ce-9fe5-6483238dcdf6', 'admin@jquiver.io', NOW(), '935b9394-c33d-4113-a248-27c46c45e7e9'),
('c9498a41-8921-44b0-a5af-7743d7e659e5', 'helpManual', '/images', 'Dev_Mode_Rest_1.png', '7ddfd364-02d5-4dfe-8400-fef79a4d39eb', 'admin@jquiver.io', NOW(), '935b9394-c33d-4113-a248-27c46c45e7e9'),

('ce49087d-106f-4bdc-bd67-5aa71e619b90', 'helpManual', '/images', 'Dev_Mode_TempUpload_1.png', 'f338cb62-c643-46c2-a58d-f129fbccc993', 'admin@jquiver.io', NOW(), '935b9394-c33d-4113-a248-27c46c45e7e9'),
('88950dfd-c8c7-48bf-bbad-6a8a8df67209', 'helpManual', '/images', 'Dev_Mode_UploadForms.png', '79dcb74e-f305-403b-bc06-8cfa80a2f6cd', 'admin@jquiver.io', NOW(), '935b9394-c33d-4113-a248-27c46c45e7e9'),
('a556eeae-82ce-4c30-9342-a715a9e28c33', 'helpManual', '/images', 'Dev_Mode_UploadDash_1.png', '76a8178a-975e-439d-a2d5-6ffe80f9e384', 'admin@jquiver.io', NOW(), '935b9394-c33d-4113-a248-27c46c45e7e9'),
('b171c780-199c-443b-adf5-5166c604e2e8', 'helpManual', '/images', 'Dev_Mode_UploadRestAPI_1.png', '6e28e77a-b48a-48a7-b259-d8911d21639e', 'admin@jquiver.io', NOW(), '935b9394-c33d-4113-a248-27c46c45e7e9'),
('0141f87a-4a0f-4cb3-9456-3a9487b50b68', 'helpManual', '/images', 'Dev_Mode_TempDownloadUpload_1.png', '2dae07b3-2a5d-4ebe-afcd-7d2da8ff9e41', 'admin@jquiver.io', NOW(), '935b9394-c33d-4113-a248-27c46c45e7e9'),
('109cbd35-3656-489a-a8d8-d2837b8160ca', 'helpManual', '/images', 'Dev_Mode_UploadDownloadForms.png', '868cd2b5-ee8b-41b8-bf78-4e30ca75985e', 'admin@jquiver.io', NOW(), '935b9394-c33d-4113-a248-27c46c45e7e9'),
('d3816cab-ce34-4c70-a3ad-aa2277f6198', 'helpManual', '/images', 'Dev_Mode_UploadDownloadDash_1.png', 'a2dd3a14-3e14-4fff-8fa6-b3c27085b6da', 'admin@jquiver.io', NOW(), '935b9394-c33d-4113-a248-27c46c45e7e9'),
('90c87a4f-743c-4a56-8118-6346a531aead', 'helpManual', '/images', 'Dev_Mode_UploadDownloadRestAPI_1.png', 'bc056b83-6278-4cca-ba91-56da39b4932e', 'admin@jquiver.io', NOW(), '935b9394-c33d-4113-a248-27c46c45e7e9'),
('ff40843a-54a2-4d5d-8923-2a9a01ab871b', 'helpManual', '/images', 'Dev_Mode_Template_Folder.png', 'ee665627-70e7-441f-ad51-9bb9c0b1ce8d', 'admin@jquiver.io', NOW(), '935b9394-c33d-4113-a248-27c46c45e7e9'),
('248ad2c0-2ab0-4a44-81bc-df9accc4ca36', 'helpManual', '/images', 'Dev_Mode_DynamicForm_Folder_1.png', 'fbbbdd8c-2744-463b-9e23-f4b7a4b6d03b', 'admin@jquiver.io', NOW(), '935b9394-c33d-4113-a248-27c46c45e7e9'),
('18c9fdd3-3d7e-4d7c-8874-6e31ea9a9aef', 'helpManual', '/images', 'Dev_Mode_DynamicForm_Folder_2.png', '7f4a81ba-9652-4abd-9c33-5c09a5b959b8', 'admin@jquiver.io', NOW(), '935b9394-c33d-4113-a248-27c46c45e7e9'),
('00c75941-bcd3-4fe4-92e7-f12503dee6fc', 'helpManual', '/images', 'Dev_Mode_DynamicRest_Folder_1.png', '0774f49f-c9f5-4386-ae2e-171d50ff64e1', 'admin@jquiver.io', NOW(), '935b9394-c33d-4113-a248-27c46c45e7e9'),
('299b9053-0616-4f52-a9ec-2280469c361e', 'helpManual', '/images', 'Dev_Mode_DynamicRest_Folder_2.png', '8ca99fae-23b2-4ad0-9ddc-de1116e3ba2d', 'admin@jquiver.io', NOW(), '935b9394-c33d-4113-a248-27c46c45e7e9'),
('acccd191-3554-4f60-92e0-5ff945ac29bd', 'helpManual', '/images', 'Dev_Mode_Dashlets_Folder_1.png', '663bea0e-232e-4e2b-9dc8-4feb0bca7707', 'admin@jquiver.io', NOW(), '935b9394-c33d-4113-a248-27c46c45e7e9'),
('0390d488-bd3a-4422-b901-61d33d4313a3', 'helpManual', '/images', 'Dev_Mode_Dashlets_Folder_2.png', 'edfe47ef-fe26-4215-9247-7f336f8bdb97', 'admin@jquiver.io', NOW(), '935b9394-c33d-4113-a248-27c46c45e7e9');

SET FOREIGN_KEY_CHECKS=1;


