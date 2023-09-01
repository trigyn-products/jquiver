SET FOREIGN_KEY_CHECKS=0;

replace into jq_manual_type (manual_id, name, is_system_manual) VALUES ('07cf45ae-2987-11eb-a9be-e454e805e22f', 'JQuiver Manual', 2);

/*************************************************JQuiver - start****************************************************************/
REPLACE INTO jq_manual_entry (manual_entry_id, manual_type, entry_name, entry_content, sort_index, last_modified_on, last_updated_by) VALUES
('57bdec61-325a-4487-9ba6-de218207f8c0', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'JQuiver', '# Purpose 
Trigyn has been working with number of Web Application project, which consists of components like Grids, Forms, Templating. In-order to speed up the initial project setup and components configuration there was a need to develop a reusable component. 

# Including Jar

```
<!-- https://mvnrepository.com/artifact/com.trigyn/java-web-starter --> 
<dependency>
    <groupId>com.trigyn</groupId>
    <artifactId>java-web-starter</artifactId>
    <version>latest-version</version>
</dependency>
```

# Modules
Maven Group-id :- com.trigyn
1. **DBUtils** : - (dbutils) In our every project, we need to configure our data-source and beans which will interact with our underlying database, this jar configures all those for you. JDBC templates, Named JDBC Templates, Hibernate Configuration, this jar also provides a script migrator tool Flyway which can be used in application to migrate scripts and maintain database changes.
2. **DB Resource Bundle / Multilingual** : - (resourcebundle) This jar is responsible to provide the application with localization and internationalization support; it will create resource-bundle tables and language tables for you.
3. **Templating Engine** : - (templating) Many web applications contain certain structure or pattern to be followed while display views, sending out mails, templating jar becomes handy in that case.
4. **Grid Utils** : - (gridutils) While developing any web application, there is always a need to show listing page, wherein the user can sort the data, filter it and keeping the performance of application in mind the data should be loaded lazily. JQuiver provides all this as a part of Gridutils jar package.
5. **Notification** : - (notification) With notification jar, you can create notifications for your application and control the duration and context with ease. You can use it for displaying notification on multiple platforms as well.
6. **Typeahead** : - (typeahead) In web-applications there is always a need to have a search box, where user can search his/her data with typing key words, JQuiver provides the jar for this functionality too. Not just single select, typeahead also give you provision for selecting multiple options.
7. **Form Builder** : - (dynamicform) Suppose there is a need to build a simple form, where you have to perform CRUD operations on one or two entities (usually for master tables), you can leverage the dynamic form jar features provided by JQuiver.
8. **Dashboard** : - (dashboard) Just like in JIRA wherein you create dashboard, and add multiple widgets so that you can view the status of your tickets or sprints, if there is a need of such components in your application, Dashboard jar has it covered.
9. **Java-Web-Starter** : - (java-web-starter) If you are using all the above components extensively in your web application, so there will be a need of an admin panel, wherein you can see and manage all your application grids, DB resource bundle, templates (views) dynamic forms, notifications, java-web-starter jar provides you with the UI where you can manage all these functionalities.
10. **REST Builder** : - (dynamic-rest) 
11. **User Management** :- (usermanagement)

', 0, NOW(), 'admin@jquiver.com');
/****************************************************JQuiver - End****************************************************************/

/*************************************************DB Utils - Start****************************************************************/
REPLACE INTO jq_manual_entry (manual_entry_id, manual_type, entry_name, entry_content, sort_index, last_modified_on, last_updated_by) VALUES
('67b91245-e86c-4b9b-9aa9-7f373916d1c5', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'DB Utils', '# DB Utils

Configuring datasource in application using application.yml : 

```
server:
  port: 8080
  shutdown: graceful
spring:
  servlet:
    multipart:
      enabled: true
      max-file-size: -1
      max-request-size: -1
  application:
    name: adc-demo
  flyway:
    cleanDisabled: true
    enabled: true
  datasource:
    driver-class-name: org.mariadb.jdbc.Driver
    url: jdbc:mysql://localhost:3306/demoportal09?createDatabaseIfNotExist=true&serverTimezone=UTC&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false
    username: root
    password: root
logging:
  level:
    root: info
    org:
      flywaydb: info
```

Database utils provides the database and hibernate configuration. In order to import and use the database utils, you need to include the dbutils jar in your pom.xml.

```
<dependency>
	<groupId>com.trigyn</groupId>
	<artifactId>dbutils</artifactId>
	<version>latest-version</version>
</dependency>
```

Once you have added the jar to your pom.xml you can extend your Repository classes with DBConnection.java class and autowire the datasource.

```
@Repository
public class TemplateDAO extends DBConnection { 

@Autowired
public TemplateDAO(DataSource dataSource) { super(dataSource); }

}
```

DB Utils also provides you with flyway configuration. 
**P.S** All the JQuiver script migrations are done using Flyway. Version scripts has the version between V0.0.0 -- V0.9.9. The migration script file location is resources/db/mysql User can override or customize the flyway configuration using the below code.

```
@Bean
public FluentConfiguration flywayConfiguration(DataSource dataSource) { FluentConfiguration configuration = Flyway.configure().dataSource(dataSource).locations("db/migration");
configuration.cleanDisabled(Boolean.TRUE);
configuration.placeholderReplacement(Boolean.FALSE);
configuration.ignoreFutureMigrations(Boolean.TRUE);
configuration.outOfOrder(Boolean.TRUE);
configuration.ignorePendingMigrations(Boolean.TRUE);
configuration.ignoreMissingMigrations(Boolean.TRUE);
return configuration;
}

```

DB Utils also contains a table **property_master**, which is used as a configuration table for application. Please refer **Application configuration** manual for more details on this.', 1,  NOW(), 'admin@jquiver.com'); 

/****************************************************DB Utils - End****************************************************************/



/*********************************************Application Configurations - Start***************************************************/

REPLACE INTO jq_manual_entry (manual_entry_id, manual_type, entry_name, entry_content, sort_index, last_modified_on, last_updated_by)
VALUES('918676c8-b653-43ee-964a-d4faaeb13787', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'Application Configurations', '# Property Master Details

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


To get the property master details in your application autowire either of the below classes :- 

```
@Autowired
private PropertyMasterDetails propertyMasterDetails = null;

@Autowired
private PropertyMasterService propertyMasterService = null; ```

You can run JQuiver Application in 2 modes, 1. Development Mode. (dev) 2. Production Mode. (prod)

By default the production mode is enabled. In this mode the templates, and forms are editable only through the UI-Editor provided by the JQuiver application. 
Whereas dev mode enabled the user to download the Template, Form Builder templates to your local machine and enables you edit it locally. This modes can be edited using **profile** property in Application Configurations module.


 ![](/cf/files/40289d3d763887410176388995320000) 


**Mail Configuration**

JQuiver has in-built classes and methods to configure mails.
To enable mail configuration goto "Mail Configuration" page, through Application Configuration

Here provide the details like, SMTP Host, SMTP Port, Security Protocol, as per your mail server.

![](/cf/files/5d45f43e-5ba0-471a-8fa5-80de72d12eb0)

This page also allows you to set the "From" and "Reply To" mail ids.

Once this is done, create a java class to invoke the mail service.

** Step 1:**Autowire SendMailService in your class
 	@Autowired
	private SendMailService sendMailService = null;

**Step 2: **Create instance of com.trigyn.jws.webstarter.utils.Email class
	Email email = new Email();

Following are the properties of Email class:

| Property Name | Type | Required |
| -------- | -------- | -------- |
| mailFrom     | String     | Yes     |
| mailFromName     | String     | No     |
| internetAddressToArray     | array of InternetAddress(javax.mail.internet) objects      | Yes     |
| body     | String     | Yes     |
| subject     | String     | Yes     |
| mailContent     | String     | No     |
| contentType     | String     | No     |
| isReplyToDifferentMail     | Boolean     | No     |
| replyToDifferentMailId     | InternetAddress     | No     |
| internetAddressCCArray     | array of InternetAddress(javax.mail.internet) objects     | No     |
| internetAddressBCCArray     | array of InternetAddress(javax.mail.internet) objects     | No     |
| isMailFooterEnabled     | Boolean     | No     |
| mailFooter     | String     | No     |
| attachementsArray     | List of files     | No     |


**Step 3: **Call sendTestMail of SendMailService class. It requires only one parameter i.e. instance of Email class.

  sendMailService.sendTestMail(email);
	Using this, you can construct the email object, with the above parameters and send the mail.
	
	OR	
	sendMailService.sendTestMail(email, jsonString);
If at all, any parameters set in Mail Configuration, page has to be overridden on the fly, then above method has to be called.

Below is a code snippet, utilising the mail service.

```
public class MailService {

	@Autowired
	private SendMailService			sendMailService			= null;

	public void sendMail(HttpServletRequest a_httpServletRequest, Map<String, Object> dAOparameters,
			UserDetailsVO userDetails) throws Exception {

		try {
			String						employeeDetails		= a_httpServletRequest.getParameter("sendMailRequestJSONStr");
			Gson						 gson				= new Gson();
			List<Map<String, String>>	listOfUsers			= gson.fromJson(employeeDetails, List.class);

			String						templateContent		= a_httpServletRequest.getParameter("mailBody");
			String						templateSubject		= a_httpServletRequest.getParameter("mailSubject");
			
			for (Map<String, String> user : listOfUsers) {
				Email				email		= new Email();
				Map<String, Object>	modelMap	= new HashMap<String, Object>();
				email.setSubject(templateSubject);
				email.setInternetAddressToArray(InternetAddress.parse(user.get("emailId").trim()));				
				email.setBody(templateContent);
				sendMailService.sendTestMail(email);
			}// End of for loop

		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
 ```
 
Application configuration also provides you with enabling Google Analytics, Create a Google Analytics Key and add it in **google-analytics-key** and also you need to set the property of **enable-google-analytics** to **true**
 
 ![](/cf/files/40289d3d763887410176388b36b60001)', 2, NOW(), 'admin@jquiver.com');
 
/*********************************************Application Configurations - End***************************************************/
 
 
 
/****************************************************User Management - Start*****************************************************/

REPLACE INTO jq_manual_entry (manual_entry_id, manual_type, entry_name, entry_content, sort_index, last_modified_on, last_updated_by)
VALUES('e03447c8-eaa0-4119-b97e-b802bd8f4ff1', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'User Management', '# User Management 
User management utils provides  user authentication through various authentication types.It also provides ACL. In order to  use these, you need to include the usermanagement jar in your pom.xml.

```
<dependency>
	<groupId>com.trigyn</groupId>
	<artifactId>usermanagement</artifactId>
	<version>latest-version</version>
</dependency>
```


These module provide following features ->  
* Manage roles -
* Manage users - 
* Manage permissions - 

![](/cf/files/4028b8817646ed030176480e43ab0026)



** Manage roles** 
  There are 3 predefined system roles  : ADMIN, AUTHENTICATED, ANONYMOUS  which you can''t edit.
	 You can add/edit  role  and set as active or inactive depending on your use case.
	 
![](/cf/files/4028b8817646ed03017647dc3ab40021)

** Manage users** 
	 There is 1 predefined user which can''t be modified : admin user
	While  add/edit user by default AUTHENTICATED role will be assigned and disabled.
	
![](/cf/files/4028b8817646ed03017647ef2f050022)
	
	You need to enter basic user fields "name", "email"  etc..   and assign role.
	You can active/deactivate a user through "is active" field.
	You can configure whether to send mail or not while a user is created/updated, using Send Mail option.
	 If you enable "force user to change password" field  then a mail would be sent to the user
	 mail id  for changing the password, mandatorily. You won''t be able to off the Send Mail property, in this case.
	The template used for email is : "force-password-mail-subject" and "force-password-mail"
	You can modify if you need to alter it.
	
![](/cf/files/4028b8817646ed03017647f1c1330023)
 

	 
**Manage Permissions**
	 There are 2 tabs listed here - Manage Role Modules,  Manage Entity Roles
	 
	 Manage Role Modules -  Various Modules are listed  with roles associated to it, user can enable/disable to access  
	 particular module or not ex Grid Module. If user has authenitcated role and if we disable then the user having only
	 authenticated role cannot access the module. Grid Module - Cannot access the listing grid , cannot add or edit grid 
	 details.
	 
![](/cf/files/4028b8817646ed030176480a3c080024)
	 
	 Manage Entity Roles -  It contains all the entites which are made up from various modules  present above 
	 if we want to remove a particular entity access just uncheck it from the grid for that role. 
	 Only those modules will be visible whose module_type_id = 0 is in the table jq_entity_role_association
	 
![](/cf/files/4028b8817646ed030176480ce3fd0025)
	 


There are 4 types of authentication:

1) Database
2) LDAP
3) OAuth

![](/cf/files/4028b8817650f1220176511211c40002)


**Database Authentication **  There are 3 different ways to configure database authentication :
1) Password
2) Password + Captcha
3) TOTP

![](/cf/files/4028b8817650f122017651166e0d0004)

**Password **-   Email id + password login 

![](/cf/files/4028b8817650f12201765121c1300005)
 
** Password + Captcha **-  Email id + password  + captcha login 
 
![](/cf/files/4028b8817650f122017651226ab90006)
	
**	TOTP **- Email Id + TOTP login
	
![](/cf/files/4028b8817650f12201765122f1750007)
 



**OAuth** : There are 3 clients for signing through 
1) Google
2) Facebook
3) Office365
For OAuth, Force Password Change option will not be available.

![](/cf/files/4028b881765fd657017660116dac0000)


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
	 
6)  Go to jq_authentication_type table  > Oauth > change authentication_properties of google client id and secret id 


**Facebook Oauth Steps** -
1) Go to https://developers.facebook.com
2) Click on myapps    > Click on create app >Choose Build Connected Experiences and continue  >Enter App display name  and rest as default and click on create app 

![](/cf/files/4028b881765fd657017660d3c0550006)
 
3) Click on  Settings tab > Click on basic  you will get client  id and secret id

![](/cf/files/4028b881765fd657017660d441bd0007)

4)  Go to jq_authentication_type table  > Oauth > change authentication_properties  of facebook client id and secret id


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

7) Go to jq_authentication_type table  > Oauth > change authentication_properties  of office365  client id and secret id


**Note:** While restarting the application  if you dont want to maintain the session of  already  logged in users , you can do this by adding  the following property 
**server.servlet.session.persistent=false**  in  you application.properties or application.yaml file. 

Add following lines of code to get logged-in user details in any template or dynamic forms:

![](/cf/files/40289d8f76a600980176a60bfa870002)
 
 
', 3, NOW(), 'admin@jquiver.com');

/***************************************************User Management - End********************************************************/



/****************************************************Master Generator - Start***********************************************************/

replace into jq_manual_entry (manual_entry_id, manual_type, entry_name, entry_content, sort_index, last_modified_on, last_updated_by) VALUES
('61fb4be9-a197-4759-9a4c-b885ce973f46', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'Master Generator', '# Master Generator

Master Generator plays a pivotal role while creating an application by using JQuiver framework.
It helps you to design the listing page, form builder and site layout from the table you have, to support your application.

**How to make use of Master Generator**

First step to create any application will be designing your database schema and creating the table. Same will be needed in JQuiver application. To make use of Master Generator, you custom tables should be in place, firstly.
Once it is done, go to Master Generator module.

** Step 1**
Select the table from the Select Table list. This  list will contain all the tables related to your application, available in the database.
 ![](/cf/files/9e9fcdb7-f54d-4c04-aee5-76a35a0559d8)
 
 Note: If you are a JQuiver developer, the all the system tables will also be available for you, in this list.
 
 ** Step 2**
 Once the table is selected, then provide the module name as per your desire.
 
 ** Step 3**
 Next comes the designing of listing page. In this section, you can decide which all columns from your table should be visible on the listing page. Also it helps you to configure, if at all any column should be kept hidden and be not visible.
 ![](/cf/files/6cdc83c4-e2b2-4eb8-b9cb-17aff7c51a58)
 
  ** Step 4**
 If you want to include this module in Site Layout/ menu, then  that can also be  configured using "Show In Menu" option.
  ![](/cf/files/5ea356bb-88d3-4097-b44d-d96ec273ed2f)
	
 ** Step 5**
	 Now comes  the Form designing. This section will help you to design the add/edit page, for the corresponding table you have selected. 
	 For configuring Form, Display Name and URL is mandatory, if you are thinking to include this in menu.
	 Provide display name, url and Parent Module Name as per your requirement.
 ![](/cf/files/c3779826-2f8b-42d1-afeb-8302482f468f)
		 
	 Now as in listing, here also you can select the fields which should be visible, which should be hidden and which should not be considered, at all, which doing the add/update of data.	 
  ![](/cf/files/97df2f91-924d-4d86-b999-72f43bf4c2bc)
	
 ** Step 6**
	 Now comes  the main part. Permissions.
	 As in all module, you can provide permission to this pages as well. Using this, you can restrict the usage of the page by the users as per their roles.	 
     ![](/cf/files/d560cd3c-82f8-4c7b-927b-77c679cdcf51)
		
 **Note:-** Before saving, do a cross verification on the entries and options you have made, while creating the master generator, as we have not provided edit option for this module. Once created, you will not be able to edit it.
		
**	Output**
 Once this module is created, the menu will be available in the application as shown below.
 ![](/cf/files/8d1522cb-a12a-474e-a870-2d63098b620b)
	
Clicking on the menu will redirect to the department listing page.	
![](/cf/files/e8534c20-a6cf-456a-82bc-8290c4a14792)
	
As in the above image, the listing page will have two options. One is Create New and another is edit. This are the feature, which avoid us to create separate pages for create and update.
	
Opting for Create New, will enable you to create a new department. This page will have only the fields which you have marked as visible/ hidden.
![](/cf/files/66b7db46-3bbf-40fc-b0aa-24ae8e3febba)
	 
 Opting for Edit, will enable yo to edit already existing entries.
![](/cf/files/b0313ee9-32b5-42bc-8b8c-9ad79c942c42)

Logically, if we create a Master Generator, entries in 3 modules are done.
1. Template
2. Dynamic Form
3. Site Layout.

Taking an example of above scenario, 
* listing page for department will be created in Templates. You can update the template to customize the page as per your requirement.
![](/cf/files/68d76985-5616-4fb4-a481-b25fd6ccf888)
![](/cf/files/a3f74af1-8159-4134-942f-5d4655d4203e)
* Add/Edit form is create in Dynamic Form. To customize this page, dynamic form has to be modified.
 ![](/cf/files/a6b9c378-6ffc-4874-870e-271d829103cf)
  ![](/cf/files/f625d2c2-6073-46a3-adc0-64d903aeaaa7)
* To add a menu entry, entry in site layout is done through Master Generator.  
 ![](/cf/files/a501ef3d-bcc9-4d42-8990-99ddeb97fbe4)
 ![](/cf/files/88260a66-eaaf-40fb-b383-518e131cb6f9)
		 
', 4, NOW(), 'admin@jquiver.com');

/****************************************************Master Generator - End******************************************************/



/****************************************************Site Layout - Start*********************************************************/

REPLACE INTO jq_manual_entry(manual_entry_id, manual_type, entry_name, entry_content, sort_index, last_modified_on, last_updated_by) VALUES
('68613ad7-8596-4c48-94e6-f752ab53eb4e', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'Site Layout', '# Site Layout

You just need to add DBUtils as a dependecy in your application to enable site layout.

Steps to create insert new entry in menu.

**Step 1: **Navigate to Site Layout module from home page.
![](/cf/files/1f178e13-be12-4c29-bc47-9910a867af6c)
 
**Step 2: **Click on Add module button
![](/cf/files/dc8c094d-700b-40a9-8d7c-49e1aa55e1dd)

**Step 3: **Provide all mandatory details and click on save button.
![](/cf/files/00b01a2d-bdeb-40aa-b457-169988480295)

**Note: **
While adding new module in menu make sure there is no other record exist with same module name or with same module URL. 
If include in menu enabled then sequence number is unique in selected parent module.

You can create module for any one of the following context type:
* Dashboard
* Form Builder
* REST API
* Template
* Root
Except for the Root as a context type, context name autocomplete will be enabled. From context name you can select any entity created from the respective context type master.

Example to include dashboard in the site layout:

**Step 1:** Create new Dashboard from the dashboard master (You can refer Dashboard help manual for more information on how to create/manage dashboard)
 ![](/cf/files/09f4260c-0cef-4569-b806-7589ee2a108f) 
 
**Step 2:** Enter Module name and enable include in menu radio button.
![](/cf/files/6011972c-0544-4322-8bb8-6229463682cb) 

**Step 3:** Select Dashboard as a context type.
![](/cf/files/c288ea4f-e848-425a-9e1a-c0284d447079)

**Step 4:** Enter dashboard name that you have created in step 1.
![](/cf/files/5d4065f3-78d7-4480-be77-ac3f35dff0ff)
 
**Step 5:** Enter Module URL.
![](/cf/files/b0c436b2-4c76-49f7-b960-a537c09e7d21)



Example to include Dynamic Form in the site layout:


**Step 1:** Create new Dynamic Form using Form Builder (You can refer Form Builder help manual for more information on how to create/manage dynamic form)
![](/cf/files/c4fd6ce2-5071-4962-bc8f-56bfce3b2907) 
 
**Step 2:** Enter Module name and enable include in menu radio button.
![](/cf/files/9f441835-28b8-425d-b21f-abbced54dfa2) 

**Step 3:** Select Form Builder as a context type.
![](/cf/files/da04a076-ed62-4f42-b1e8-91372a3e0fc4)

**Step 4:** Enter form name that you have created in step 1.
![](/cf/files/6f796eb0-bafa-4dcd-9c8a-d2469be048fe)
 
**Step 5: ** Enter Module URL.
![](/cf/files/f24d2e80-8b38-48a0-bfac-5a2488736086)


Example to include REST API in the site layout:


**Step 1:** Configure new record in REST API master(You can refer REST API help manual for more information on how to create/manage REST API)
![](/cf/files/717c32c4-be30-47d7-b55e-25e11b20b195)
 
**Step 2:** Enter Module name and enable include in menu radio button.
![](/cf/files/59ad8aa2-fcbe-4ff8-85b4-b92fb09d2b81)

**Step 3:** Select REST API as a context type.
![](/cf/files/fc522253-f73a-4980-a43b-c096d82e6081) 

**Step 4:** Enter method name that you have created in step 1.
![](/cf/files/a3f2c013-dd80-4cd0-957f-a3ae8da725e1)
 
**Step 5: ** Enter Module URL.
![](/cf/files/3860998e-33ab-4dcb-a27d-0cce0aa73337)



Example to include Template in the site layout:


**Step 1:** Create new template from Templating module(You can refer Templating help manual for more information on how to create/manage Template)
![](/cf/files/2942ed27-f194-459a-8121-0106b9d3d9a4)
 
**Step 2:** Enter Module name and enable include in menu radio button.
![](/cf/files/72d8474a-7d4d-4aa8-8528-833679baf9be) 

**Step 3:** Select Template as a context type.
![](/cf/files/a3f4f471-0386-40ee-b21b-e91841083822)

**Step 4:** Enter template name that you have created in step 1.
![](/cf/files/649fab90-76a9-4135-b271-71b9388140a7) 
 
**Step 5: ** Enter Module URL.
![](/cf/files/a3e471fd-f9be-4987-bb4f-9db93a9cbb06)


Steps to create group of modules:

**Step 1: ** Create new parent module
![](/cf/files/93d0e859-4726-41ea-af6d-743258cbecd1)

**Step 2: ** Create child module and select module name created in step 1 as parent module
![](/cf/files/758ba2cd-ff8a-4865-94b2-4d062ffab93c)
![](/cf/files/553b2332-bf8b-462b-9c5c-d89bc0682a92)

Example:
![](/cf/files/b3fd7730-a501-4a50-afed-54a956151748)


Step to configure role based home page:

**Step 1:  **Create new role. Navigate to User management from home page.

**Step 2:  **Click on Manage Roles
![](/cf/files/577dfda4-41cb-4c8a-ad9d-bdb769140d36)

**Step 3:  **Click on Add Role
![](/cf/files/f207f7fb-0a09-471d-b17d-93f1b55f0f86)

**Step 4:  **Enter data in all mandatory fields and Role Priority should be unique. Click on save button
![](/cf/files/ac0a0f40-cbc8-4f52-8125-a2d7808a2ff4)

**Step 5:  **Once role has been created navigate to Site Layout.

**Step 6:  **Click on Set Default Page
![](/cf/files/0a7a4f60-8819-43fd-aa85-90a02bb042ba)

**Step 7:  **Edit newly created role
![](/cf/files/4daeff56-7cbc-4f9b-ba29-3c60021ec634)

**Step 8:  **Select module name
![](/cf/files/29f15fe9-bd19-4f4a-a4c9-ec198a9b078b)

**Step 9:  **Click on save button
![](/cf/files/9656a918-91c6-4352-8dbb-07860e404e53)

**Step 10:  **Now go back to User Management. 

**Step 11:  **Click on Manage Users.
![](/cf/files/ecbb9c05-228b-4a0d-b65c-202891905318)


**Step 12:  **Edit any user details.
![](/cf/files/44c3eaa9-82a7-4c66-8ba5-af20ab5ff087) 

**Step 13:  **Enable newly created role and save the details
![](/cf/files/ecb167a8-bb55-4c23-be00-32b88e8903d1) 


Example:
![](/cf/files/56ebcae1-14b7-4855-b5d5-9e936c961347)

In case you have configured home page for more than one role then system will render home page based on role priority.


| Role Name | Home Page | Home Page |
| -------- | -------- | -------- | 
| DEVELOPER     | Developer Dashboard    | 4     |
| SIT   | SIT Dashboard     | 5     |
| UAT  |  UAT Dashboard  | 6     |

Suppose you have above configuration in place and Joe has all three roles so when Joe will login into the system, system will render UAT dashboard for him as a home page.

Steps to create module url with path variables:

**Step 1: ** Create new template and access the path variables using freemarker list directives:

<#noparse>
<#if pathVariableList?? && pathVariableList?size != 0>
	<#list pathVariableList as pathVariable>
    	${pathVariable?cap_first}
	</#list>
</#if>
</#noparse>

![](/cf/files/b92c317e-d2fd-44b5-a972-9ca18af4705d)

**Step 2: ** Create new module in Site layout and save Module URL with path variables
![](/cf/files/8aa4e0ca-f775-404e-972f-396bf639390e) 

Example:
![](/cf/files/c41a3346-5a70-4c56-b015-76f527c389cd)

** Validations: **
  
* Except for Root as context type, Module URL must be unique.
* If Include In Menu is enable and if Parent module is Root then Sequence must be unique at parent level or else if Parent module is other than Root then Sequence shoud be unique in that particular group. 
', 5, NOW(), 'admin@jquiver.com');


/****************************************************Site Layout - End***********************************************************/



/****************************************************Grid Utils - Start**********************************************************/

REPLACE INTO jq_manual_entry(manual_entry_id, manual_type, entry_name, entry_content, sort_index, last_modified_on, last_updated_by) VALUES
('7428a452-da97-4ef0-b6d7-acf4921beb82', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'Grid Utils', '# Grid Utils

```
<dependency>
	<groupId>com.trigyn</groupId>
    <artifactId>gridutils</artifactId>
    <version>latest-version</version>
</dependency>
```

This will import dbutils jar implicitly and will create jq_grid_details table. The jar will provide an URL ***server-name***:***port***/cf/grid-data which will be the data-provider for the grid. The user needs to create a stored procedure which will return the data for grid, the details needs to be inserted into grid_tables as follows: - `grid_id` : The unique identifier for your grid, this will be used to be called from UI  `grid_name` : The name of the grid.  `grid_description`  : The description stating the purpose of this grid. `grid_table_name` : This column will have your stored procedure name which will be called to get the data for grid. `grid_column_names` : This are the comma separated names of columns (aliases mentioned in your SP) 
While creating the SP make sure you add following input parameters,    
* forCount INT,
* limitFrom INT,
* limitTo INT,
* sortIndex VARCHAR(100),
* sortOrder VARCHAR(20),
* langCode VARCHAR (20)


We have added an example demonstrating the use of the grid utility. Stored procedure reference gridDetails. 

Steps to create grid details.

**Step 1: -**
 ![](/cf/files/40289d3d7629c27501762a22257f0012) 
 
**Step 2 :- **Add a new entry.
![](/cf/files/40289d3d7629c27501762a22341b0013) 

**Step 3 :- **Fill in the required details , Grid Table name can have table name, view name and Stored Procedure name as a value, and columns names are comma seperated values.
![](/cf/files/40289d3d7629c27501762a223f2e0014) 

**Step 4 :- **After creating the grid entry, go to templates modules from Jquiver home page and click on add template, a form will get opened, enter the template name in which the grid will be displayed and select the default-template-listing from Default Template drop-down, on selecting it will create the basic template for your grid listing page.
![](/cf/files/40289d3d7629c27501762a2258450015)

**Step 5 :- **Modify the col models accordingly in template, and pass the appropriate values in the grid initialization. 

Parameters to be passed, gridId, additionalParams, colModels.

**Passing parameters to grid **
If you want to provide specific criteria to load a grid, following can be taken into consideration.

```
let grid = $("#divAutocompleteGrid").grid({
	      gridId: "autocompleteListingGrid",
	      colModel: colM,
          dataModel: dataModel,
          additionalParameters: {"cr_autocompleteTypeId":"str_1"}
	  });
```

Here you can have to pass parameters to additionalParameters option, as below.
str has to be appended as prefix to the value and cr to the column name.
		The syntax will be "cr_<column_name>" : "str_<param_value>"
		
If any grid has to be loaded as per the logged in user data for a particular column, then below has to be done
```
additionalParameters: {"cr_createdBy":"str__loggedInUserName"}
```
Here createdBy will be the column name, at rest should be kept as it is and the system will take care of the need.

', 6, NOW(), 'admin@jquiver.com');

/****************************************************Grid Utils - End************************************************************/



/****************************************************Templating - Start**********************************************************/

REPLACE INTO jq_manual_entry(manual_entry_id, manual_type, entry_name, entry_content, sort_index, last_modified_on, last_updated_by)
VALUES('17feffba-99f4-4591-9cb5-0fef46ee0b77', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'Templating', '# Templating 

In order to import the templating jar, add following dependency in your pom.xml.

```
<dependency>
	<groupId>com.trigyn</groupId>
	<artifactId>templating</artifactId>
	<version>latest-version</version>
</dependency>
```

This will also import resourcebundle and dbutils jar implicitly, as these templates can be used as views in an application and so we might need to support localization and internationalization. In order to process the template with the given set of data, Autowire TemplatingUtils class, this class will render the templates and will add some supporting data in it like, contextPath, messageSource (in-order to evaluate resource-bundle keys.) so in-order to get the label in local language.

Java code to process template
There are many methods available in menu service to process a template. Call appropriate method depending on your use case. 


| Method Name | Parameters | Layout included | Description |
| -------- | -------- | -------- | -------- |
| getTemplateWithSiteLayout     | Template name and Template Parameters     | Yes     | To display template with layout     |
| getTemplateWithSiteLayoutWithoutProcess     | Template name and Template Parameters     | Yes     | To display template content without processing it.    |
| getTemplateWithoutLayout     | Template name and Template Parameters     | No     | To display template without layout     |
| getDashletWithLayout     | Dashlet name and Template Parameters     | Yes     | To display dashlet with layout     |
| getDashletWithoutLayout     | Dashlet name, Dashlet Content and Template Parameters     | No     | To display dashlet without layout     |




**Step 1: **Autowired Menu Service
@Autowired
private MenuService menuService 			= null;

**Step 2: **Call method of menu service with required parameters
String processedTemplate = menuService.getTemplateWithSiteLayout(template-name,  templateData);


Default macros provided are :- 

| Macros | Parameters |
| -------- | -------- |
| templateWithoutParams     | Template-Name     |
| templateWithParams     | Template-Name, Template-Parameters     |
| resourceBundle     | Resource-Key     |
| resourceBundleWithDefault     | Resource-Key, Default-Value  |


**Default macros example :-
Template without parameters:**

Syntax:
<#noparse><@templateWithoutParams templateName/>

Example:
<@templateWithoutParams "role-autocomplete"/>

![](/cf/files/4028168b7647e50a0176488ec8860003) 

**Template with parameters:**
Syntax:
Create freemarket variable.
<@templateWithParams templateName freemarker-variable-name />

Example:
<#assign gaAttributes = {
"enableGoogleAnalytics": enableGoogleAnalytics,
"googleAnalyticsKey": googleAnalyticsKey,
"entityType": entityType,
"entityName": entityName
}>
<@templateWithParams "google-analytics-template" gaAttributes />

![](/cf/files/4028168b7647e50a0176488f32110004)

Resource Bundle without default
Syntax:
<@resourceBundle resource-key/>

Example:
<@resourceBundle "jws.addProperty"/>

![](/cf/files/4028168b7647e50a0176488f4ebe0005) 

Resource Bundle with default
Syntax:
<@resourceBundleWithDefault resource-key default-text/>

Example:
<@resourceBundleWithDefault "jws.addProperty" "Add property"/></#noparse>

![](/cf/files/4028168b7647e50a0176488f60ba0006)


**Note** : - Importing the jar will create jq_template_master table in your application schema.

References for Freemaker :- https://freemarker.apache.org/ ', 7, NOW(), 'admin@jquiver.com');

/****************************************************Templating - End************************************************************/



/****************************************************Dynamic Form - Start********************************************************/

REPLACE INTO jq_manual_entry(manual_entry_id, manual_type, entry_name, entry_content, sort_index, last_modified_on, last_updated_by)
VALUES('6938f0ac-00fe-4b94-95e7-02ef72016fe4', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'Form Builder', '# FormBuilder 

In order to import this jar, add following dependency in your pom.xml

```
<dependency>
	<groupId>com.trigyn</groupId>
	<artifactId>dynamic-form</artifactId>
	<version>latest-version</version>
</dependency>
```

These will import the form builder jar and will create jq_dynamic_form and jq_dynamic_form_save_queries tables. Table jq_dynamic_form  used for storing the  form details such as form html and select  query to fetch data for existing records, while jq_dynamic_form_save_queries is used for storing the save/update queries.

Dynamic form can be used to create simple forms , by just selecting the table we get the html form ready, save query etc. This form can be user for add as well as  editing it .

**To add a new dynamic-form follow steps: **  

**Step 1: ** Open Dynamic Form from home page.
![](/cf/files/4028b88176a8f81a0176a9aad1090000)
**Step 2: ** After that all dynamic forms will be listed where we can add/edit  form. 
![](/cf/files/4028b88176a8f81a0176a9ae0cf80001)
**Step 3:** After clicking on add , Enter form name, and select value from Form template  around which form would be generated.
![](/cf/files/4028b88176ae0bd20176ae160c010000)
**Step 4:** After selecting the table , select script , html and save script would be populated.
![](/cf/files/4028b88176ae0bd20176ae1abd670002)
![](/cf/files/4028b88176ae0bd20176ae1843480001)
![](/cf/files/4028b88176ae0bd20176ae1bbc760003)
		 
**Step 5:** All the populated fields can be altered if required.
**Note:** Form field names should match with the save query params.
Example:
```
<input type="text" data-type="varchar" id="formdescription" name="formdescription"  value="" maxlength="100" class="form-control">
			
UPDATE jq_dynamic_form SET form_name = :formname,form_description = :formdescription  WHERE form_id = :formid
```
			
If the primary key is of varchar type then `UUID()` is used as primary key, if auto increment then  `SELECT CASE WHEN MAX(authentication_id) IS NULL THEN 1 ELSE MAX(authentication_id) + 1` is used as primary key.

**Step 6: **  To use form ie. add a new record or edit a record below code format can be used:
```
<form id="addEditNotification" action="${(contextPath)!''''}/cf/df" method="post" class="margin-r-5 pull-left">
<input type="hidden" name="formId" value="e848b04c-f19b-11ea-9304-f48e38ab9348">
<input type="hidden" name="primaryId" id="primaryId" value=""> 
<button type="submit" class="btn btn-primary">
                Add Notification
            </button>
</form>
```
For new record  primaryId should be blank, for edit it should contain the value.


To enable captcha in any form add following piece of code in html and JS ready function:
```
<div class="col-3">
	<p>
		<img id="imgCaptcha" name="imgCaptcha" src="${(contextPath)!''''}/cf/captcha/<#noparse>${formId}</#noparse>_captcha">
		<span id="reloadCaptcha"><i class="fa fa-refresh" aria-hidden="true"></i></span>
		<label for="captcha" class="sr-only">Enter Captcha</label>
		<input type="text" id="formCaptcha" name="formCaptcha" class="form-control" placeholder="Enter Captcha"  >
	</p>  
</div>
```

```
$("#reloadCaptcha").click(function(event){
	$("#imgCaptcha").attr("src", $("#imgCaptcha").attr("src")+"#");
    $("#formCaptcha").val("");
});
```

In case of invalid captcha application will return **412** Precondition failed HTTP status code.

```
	function saveData(){
		let isDataSaved = false;
		let formData = validateData();	
		if(formData === undefined){
			$("#errorMessage").html("All fields are mandatory");
			$("#errorMessage").show();
			return false;
		}
		$("#errorMessage").hide();
		formData.push({"name": "formId", "value": formId, "valueType": "varchar"});
		formData.push({"name": "isEdit", "value": (isEdit + ""), "valueType": "int"});
		
		$.ajax({
		  type : "POST",
		  async: false,
		  url : contextPath+"/cf/psdf",
		  data : {
		  	formData: JSON.stringify(formData),
		  	formId: formId
		  },
          success : function(data) {
			isDataSaved = true;
            $("#reloadCaptcha").trigger("click");
			showMessage("Information saved successfully", "success");
		  },
	      error : function(xhr, error){
            if(xhr.status == 412){
                showMessage("Invalid Captcha", "error");
                $("#reloadCaptcha").trigger("click");
            }  
            else{
			    showMessage("Error occurred while saving", "error");
            }
	      },
		});
		return isDataSaved;
	}
```

**Note: **Make sure name attribute of input type is **formCaptcha** and src of img element is **<#noparse>${(contextPath)!''''}/cf/captcha/${formId}_captcha</#noparse>**

**How to handle custom exception messages**

Rather than providing an if-else condition on error handling part, we JQuiver support handling of exception handling messages us	ing free marker.

Below is an example of how this can be achieved in FTL. While creating a dynamic form, we provide the save update query using FTL. So in that part of dynamic form creation you can provide the messages as per the requirement.

Syntax will be
``` 
<#stop "This will contain the exception message to be rendered"> 
```

Below is a code snippet, given as an example for reference.
```
<#if isEdit == 1>
    UPDATE table_name SET column1 = :val1,client_key = :clientkey,WHERE column2 = :condition
<#else>
    <#stop "Message">
</#if>
```

Once this is done, in the error handler of ajax call, check the responseText of xhr object. This will contain your exception message provided in the "stop" command.
i.e;
```
error : function(xhr, error){
			showMessage(xhr.responseText, "error");
},
```
This will display your message.
', 8, NOW(), 'admin@jquiver.com');

/****************************************************Dynamic Form - End**********************************************************/



/****************************************************Multilingual - Start********************************************************/

REPLACE INTO jq_manual_entry (manual_entry_id, manual_type, entry_name, entry_content, sort_index, last_modified_on, last_updated_by) 
VALUES('9c25fb63-8336-4f22-bb97-a5042159d5c4', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'Multilingual', '# Multilingual
If you are finding ways to spread your market reach and attain global presence, localization of application software is of vital importance. Besides, the very nature of any software application liberalizes the user to have it accessed, bought and downloaded from any geographical location. To enable multilingual for your application just import the resourcebundle jar by adding following dependency in your pom.xml.
 
```
<dependency>
	<groupId>com.trigyn</groupId>
	<artifactId>resourcebundle</artifactId>
	<version>latest-version</version>
</dependency>
```

This jar will create jq_resource_bundle and language table in your database. You can easily add support for new language by just adding new record in language table.
**language table structure:**
	language_id: This will be the primary key
	language_name: Name of the language
	language_code:  ISO code of a language
	last_update_ts:  Last updated timestamp
	is_deleted: Possible values(0 or 1). If language is marked as 0 then it will be displayed in UI.

**jq_resource_bundle table structure::**
	resource_key: Unique key. This will be used wherever we need support for internationlization or localization.
	language_id: language id for corresponding resource key and text.
	text: Translated text. Except for English language, system will save text in unicode format.

**Step 1: **Navigate to Multilingual master.

 ![](/cf/files/40289d3d762c323c01762cc6c8a30000) 

**Step 2: **Click on Add Resource Bundle.

![](/cf/files/40289d3d762c323c01762cc6f25c0001)

**Step 3: **Add new resource key and provide translation for all languages.

 ![](/cf/files/40289d3d762c323c01762cc7015f0002)

Multilingual Demo

 ![](/cf/files/40289d3d762c323c01762cc712310003) 
 
**French language**

 ![](/cf/files/40289d3d762c323c01762cc71ee00004) 
 
**Hindi languae**

 ![](/cf/files/40289d3d762c323c01762cc72f480005)

JQuiver provides few default resource bundle keys which application developer can utilize in thier project like instead of JQuiver you can have your own project name in different template
Just update English language content for jws.projectName and it will be reflected in following templates
![](/cf/files/1fca4824-251f-4426-abdc-8cff655fbfa4) 
![](/cf/files/3a1e89b9-f0cb-42aa-ab7d-041e08daf536)
 
* Home page  - home-page (Title and Navbar)
![](/cf/files/4ab78613-7f7b-46b5-af26-73a3643aaea0) 

* Login Page - jws-login
![](/cf/files/8b01d2d4-1bd8-4a54-a460-f50202cd0471) 

* Reset Password Page - jws-password-reset-mail
![](/cf/files/8261a46a-3ece-4ff6-a81e-742ac55d1cfa) 

* Reset Password Page - jws-configure-totp
![](/cf/files/72ad938d-a6de-4d16-9203-27cc8b12f649) 

* Change Password Page - jws-password-reset-page
![](/cf/files/1bd91c6b-3f30-4481-8803-f9a8fe8b8626)
 
Following are the steps if you want to fetch data from resource bundle:

**Step 1: **Autowired Resource Bundle repository
```
@Autowired
private IResourceBundleRepository iResourceBundleRepository = null;```

**Step 2: **Use findByKeyAndLanguageCode method of Resource Bundle repository with all rquired parameters
```
String message = iResourceBundleRepository.findByKeyAndLanguageCode(resourceBundleKey,localeCode,defaultLocaleCode,isDeleted);
```

', 9, NOW(), 'admin@jquiver.com');

/*****************************************************Multilingual - End*********************************************************/



/****************************************************REST API - Start************************************************************/

REPLACE INTO jq_manual_entry (manual_entry_id, manual_type, entry_name, entry_content, sort_index, last_modified_on, last_updated_by)
VALUES('81c506ff-dab5-43de-a790-58af356de3e9', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'Rest API', '# Rest API 

Rest API lets you write API''s in below languages :
1. FTL (Freemaker)
2. Java
3. Javascript.

Follow the steps below to create a REST API dynamically.

**Step 1: **Visit the REST API module on Jquiver home.
 ![](/cf/files/40281694762864a50176288c66620001)

**Step 2 : **Click on Add REST API, it will open up a form to add/edit REST API
 ![](/cf/files/40281694762864a50176288c755a0002)
 ![](/cf/files/402816947628cb18017628cfa6110000)
 


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


**Type of queries: **

* Select Query
* Insert-Update-Delete Query
* Stored Procedure



Steps to create new REST API for FTL:

**Step 1: ** Provide unique REST URL

**Step 2: ** Select appropriate HTTP Method Type
 ![](/cf/files/c6b5c730-804f-4aab-9a22-ea8d2b32ad63)

**Step 3: ** Select appropriate HTTP Method Type 
 ![](/cf/files/29792cef-d7e7-43ba-a15a-36ec20e6949d)

**Step 4: ** Select FTL as platfrom type and write service logic using FTL directives, expression etc.
 ![](/cf/files/3ea2a9eb-81bf-4e89-b904-a29d7e6eb709)

**Step 5: ** You can write any type of query. There are no restriction on number of queries.
![](/cf/files/b3e3c0c6-62f4-41f0-9931-3ab2a378e5ee) 


** FTL Service Logic **


```
<#if manualSequenceDetailsList?? && manualSequenceDetailsList?size != 0>
    <#list manualSequenceDetailsList as manualSequenceDetails>
        <#if (manualSequenceDetails.sortIndex)?? && (manualSequenceDetails.sortIndex)?has_content>
        	${manualSequenceDetails.sortIndex}
        </#if>
    </#list>
</#if>
```

Example to call and execute FTL REST API in any template: 
![](/cf/files/6079c4c4-167a-4a16-bdee-56c9f93f986b)


  
   
   
Steps to create new REST API for Java:

**Step 1: ** Provide unique REST URL
![](/cf/files/f63b06f3-ae5d-48f0-9d9a-0556fa98c8f4)

**Step 2: ** Select appropriate HTTP Method Type
![](/cf/files/92f1b314-0c50-4c4b-aaca-39c18f003eed)

**Step 3: ** Select appropriate HTTP Produces Type 
![](/cf/files/8f2e0a09-0088-4adf-a224-4f4a50a9a894)

**Step 4: ** Enter unique method name and that method with Method Signature must be present in Java file provided in service logic
![](/cf/files/8984531a-367d-4f25-b3a8-80c7cb9c8066)

**Step 5: ** Select Java as platfrom type. You can have your own business logic in selected method. You can autowired(component,repository, service etc.) any class and execute any method of that class. Make sure to create following method in your Java file

```
public void setApplicationContext(ApplicationContext applicationContext) { }
```


**Step 6: ** You can write any type of query. There are no restriction on number of queries.
![](/cf/files/512a2273-df0e-40b2-b482-2927aee32c2c) 
![](/cf/files/89ad8745-d78a-4abf-8793-7e7001a288ca) 
![](/cf/files/78827b0f-e616-4352-b70d-11588f712a83)

**Java Service Logic**
If you choose platform id as java then you can wirite java code in Java provided in  service logic for example:

```
package com.trigyn.jws.dynarest.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.trigyn.jws.dbutils.spi.PropertyMasterDetails;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.resourcebundle.repository.interfaces.IResourceBundleRepository;
import com.trigyn.jws.resourcebundle.vo.ResourceBundleVO;
import com.trigyn.jws.templating.service.MenuService;

public class DynaRest {

	@Autowired
	private PropertyMasterDetails		propertyMasterDetails		= null;

	@Autowired
	private IResourceBundleRepository	iResourceBundleRepository	= null;

	@Autowired
	private MenuService					menuService					= null;

	/**
	 * 
	 * Method to get dynamic rest details
	 *
	 */
	public Map<String, Object> getDynamicRestDetails(HttpServletRequest a_httpServletRequest,
			Map<String, Object> dAOparameters, UserDetailsVO userDetails) {
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("methodTypes", dAOparameters.get("dynarestMethodType"));
		responseMap.put("producerDetails", dAOparameters.get("dynarestProducerDetails"));
		responseMap.put("dynarestDetails", dAOparameters.get("dynarestDetails"));
		return responseMap;
	}

	/**
	 * 
	 * Method to get default template listing
	 *
	 */
	public Map<String, Object> defaultTemplates(HttpServletRequest a_httpServletRequest,
			Map<String, Object> dAOparameters, UserDetailsVO userDetails) {
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("defaultTemplates", dAOparameters.get("defaultTemplates"));
		return responseMap;
	}

	/**
	 * 
	 * Method to get value from property master
	 *
	 */
	public String getPropertyValueFromPropertyMaster(HttpServletRequest a_httpServletRequest,
			Map<String, Object> daoResultSets, UserDetailsVO userDetails) {
		String	ownerId			= a_httpServletRequest.getParameter("ownerId");
		String	ownerType		= a_httpServletRequest.getParameter("ownerType");
		String	propertyName	= a_httpServletRequest.getParameter("propertyName");
		String	propertyValue	= propertyMasterDetails.getPropertyValueFromPropertyMaster(ownerId, ownerType,
				propertyName);
		if (StringUtils.isBlank(propertyValue) == false) {
			System.out.println("Welcome to Property Master: " + propertyValue);
		}
		return propertyValue;
	}

	/**
	 * 
	 * Method to get translation from resource bundle
	 *
	 */
	public Map<String, Object> getTranslationByKey(HttpServletRequest a_httpServletRequest,
			Map<String, Object> daoResultSets, UserDetailsVO userDetails) {
		Map<String, Object>		responseMap				= new HashMap<>();
		String					resourceBundleKey		= a_httpServletRequest.getParameter("resourceBundleKey");
		List<ResourceBundleVO>	resourceBundleVOList	= iResourceBundleRepository
				.findResourceBundleByKey(resourceBundleKey);
		if (!CollectionUtils.isEmpty(resourceBundleVOList)) {
			System.out.println("Welcome to Resource Bundle ");
			for (ResourceBundleVO resourceBundleVO : resourceBundleVOList) {
				responseMap.put(resourceBundleVO.getLanguageId().toString(), resourceBundleVO.getText());
			}
		}
		return responseMap;
	}

	/**
	 * 
	 * Method to get template content by name and process template content
	 *
	 */
	public String getTemplate(HttpServletRequest a_httpServletRequest, Map<String, Object> daoResultSets,
			UserDetailsVO userDetails) throws Exception {
		Map<String, Object>	templateMap		= new HashMap<String, Object>();
		String				templateName	= a_httpServletRequest.getParameter("templateName");
		String				templateContent	= menuService.getTemplateWithSiteLayout(templateName, templateMap);
		return templateContent;
	}

	public String getDynarestBaseUrl(HttpServletRequest a_httpServletRequest, Map<String, Object> daoResultSets,
			UserDetailsVO userDetails) {
		String			uri			= a_httpServletRequest.getRequestURI()
				.substring(a_httpServletRequest.getContextPath().length());
		String			url			= a_httpServletRequest.getRequestURL().toString();
		StringBuilder	urlPrefix	= new StringBuilder();
		url = url.replace(uri, "");
		urlPrefix.append(url).append("/api/");
		return urlPrefix.toString();
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.menuService				= applicationContext.getBean(MenuService.class);
		this.propertyMasterDetails		= applicationContext.getBean(PropertyMasterDetails.class);
		this.iResourceBundleRepository	= applicationContext.getBean(IResourceBundleRepository.class);
	}
}

```

Example to call and execute Java REST API in any template: 
![](/cf/files/84dfcea6-4f20-44c6-b33e-c76cedbf3053)

**Step 1: ** Provide unique REST URL
![](/cf/files/2e5939e8-8d84-42b5-9f92-3a9c590e5a98)

**Step 2: ** Select appropriate HTTP Method Type
![](/cf/files/ddefefe2-b9ad-4cac-871f-28cefb134208)

**Step 3: ** Select appropriate HTTP Produces Type 
![](/cf/files/c5ce38d5-b8f6-4ec1-a947-b0db7963812c) 

**Step 4: ** Select FTL as platfrom type and write service logic using JavaScript variable, functions, objects etc. Make sure to your function after its declaration.
![](/cf/files/377f1c16-03a2-4202-81d1-8151be81fb1a)

**Step 5: ** You can write any type of query. There are no restriction on number of queries.
![](/cf/files/3009a9cb-dcae-49ba-a3c2-c13ce62750ef) 

  
**JavaScript Service Logic**

```
function getFileConfigDetails(requestDetails, daoResults) {
    return daoResults["fileConfigs"][0];
}

getFileConfigDetails(requestDetails, daoResults);
```

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

', 10, NOW(), 'admin@jquiver.com');

/*****************************************************REST API - End*************************************************************/



/****************************************************Autocomplete - Start********************************************************/

REPLACE INTO jq_manual_entry (manual_entry_id, manual_type, entry_name, entry_content, sort_index, last_modified_on, last_updated_by) 
VALUES('5e46df00-e07a-4b73-889f-2894adfd3df8', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'AutoComplete', '# AutoComplete

 In order to import the autocomplete jar, add following dependency in your pom.xml.

```
<dependency>
	<groupId>com.trigyn</groupId>
	<artifactId>typeahead</artifactId>
	<version>latest-version</version>
</dependency>
```

This will import autocomplete jar implicitly and will create jq_autocomplete_details table. The jar will provide an URL server name:port/cf/autocomplete-data which will be the data-provider for the autocomplete. The user needs to write a query which will return the data for autocomplete, the details needs to be inserted into autocomplete_details as follows: -

ac_id : The unique identifier for your autocomplete, this will be used to be called from UI.
ac_description : The description stating the purpose of this autocomplete.
ac_select_query: The query to fetch data for autocomplete.

You can enhance your autocomplete or multiselect with following options:
**Prefetch:** You can speed up the operations just by adding one flag (prefetch : true) to you component. 
**Local Storage: **You can avoid server call to fetch and render data in autocomplete/multiselect using local storage. To enable local storage you have to set list of available items in autocomplete using items property.
**Enable Clear Text: ** You can add **clear text** button to your autocomplete by setting true value enableClearText. You can overwrite resetDependentInput function to reset dependent JavaScript variables, html input fields etc. 

Following are the steps to add autocomplet/multiselect.

**Step 1: **
Add following css and js.

```
	<script src="${(contextPath)!''''}/webjars/1.0/rich-autocomplete/jquery.richAutocomplete.min.js"></script>
	<script src="${(contextPath)!''''}/webjars/1.0/typeahead/typeahead.js"></script>
	<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/rich-autocomplete/richAutocomplete.min.css" />
```

**Step 2: **
Initialize autocomple/multiselect component in javascript ready function. 
 
# Example to include basic autocomplete component:
HTML Structure: 

```
	<div class="col-6">
		<div class="col-inner-form full-form-fields">
			<label for="flammableState" style="white-space:nowrap">Autocomplete</label>
			<div class="search-cover">
				<input class="form-control" id="rbAutocomplete" type="text">
				<i class="fa fa-search" aria-hidden="true"></i>
            </div>
 		</div>
	</div>
```

JS changes: 
```
<script>
let autocomplete;

$(function () {
    autocomplete = $(''#rbAutocomplete'').autocomplete({
        autocompleteId: "resourcesAutocomplete",
		pageSize: 10,//Default page size is 10
		prefetch : false,
        render: function(item) {
        	let renderStr ='''';
        	if(item.emptyMsg == undefined || item.emptyMsg === '''')
    		{
        		renderStr = ''<p>''+item.text+''</p>'';
    		}
        	else
    		{
        		renderStr = item.emptyMsg;	
    		}	    				        
            return renderStr;
        },
        additionalParamaters: {languageId: 1},
        extractText: function(item) {
            return item.text;
        },
        select: function(item) {
            $("#rbAutocomplete").blur();
        }, 	
		resetAutocomplete: function(){ 
        	//This function will be executed onblur or when user click on clear text button
        	//Code to reset dependent JavaScript variables, input fields etc.
        }, 
    }, {key: "jws.action", languageId: 1, text: "Action"});
    
    //You can set default value using setSelectedObject function
    autcomplete.setSelectedObject({key: "jws.action", languageId: 1, text: "Action"});
});

//User can reset any autocomplete component by calling resetAutocomplete function
autocomplete.resetAutocomplete();

 </script>   
```

![](/cf/files/40289d3d765124480176512a5e0f0000)

# Example to include basic autocomplete with prefetch:

HTML Structure: 

```
	<div class="col-6">
		<div class="col-inner-form full-form-fields">
			<label for="flammableState" style="white-space:nowrap">Autocomplete(Prefetch)</label>
			<div class="search-cover">
				<input class="form-control" id="rbAutocompletePF" type="text">
				<i class="fa fa-search" aria-hidden="true"></i>
            </div>
 		</div>
	</div>
```

JS changes: 
```
<script>
let autocompletePF;

$(function () {
	autocompletePF = $(''#rbAutocompletePF'').autocomplete({
        autocompleteId: "resourcesAutocomplete",
		pageSize: 10,//Default page size is 10
		prefetch : true,
        render: function(item) {
        	let renderStr ='''';
        	if(item.emptyMsg == undefined || item.emptyMsg === ''''){
        		renderStr = ''<p>''+item.text+''</p>'';
    		}else{
        		renderStr = item.emptyMsg;	
    		}	    				        
            return renderStr;
        },
        additionalParamaters: {languageId: 1},
        extractText: function(item) {
            return item.text;
        },
        select: function(item) {
            $("#rbAutocompletePF").blur();
        }, 	
		resetAutocomplete: function(){ 
        	//This function will be executed onblur or when user click on clear text button
        	//Code to reset dependent JavaScript variables, input fields etc.
        },         
    });
});    
 </script>   
```

![](/cf/files/40289d3d765124480176512a6e660001) 


# Example to include basic autocomplete with local storage:

HTML Structure: 

```
		<div class="col-6">
			<div class="col-inner-form full-form-fields">
				<label for="flammableState" style="white-space:nowrap">Autocomplete(Local Storage)</label>
				<div class="search-cover">
					<input class="form-control" id="rbAutocompleteLS" type="text">
					<i class="fa fa-search" aria-hidden="true"></i>
            	</div>
 			</div>
		</div>
```

JS changes: 
```
<script>

$(function () {
 $("#rbAutocompleteLS").richAutocomplete({
		items: [{
			languageName: "English",
			languageId: 1
		}, {
			languageName: "French",
			languageId: 2
		}, {
			languageName: "Hindi",
			languageId: 3
		}],
		extractText: function(item) {
			return item.languageName;
		},
		filter: function(items, searchTerm) {
			return items.filter(function(item) {
				return item.languageName.toLowerCase().indexOf(searchTerm.toLowerCase()) !== -1;
			});
		},
		render: function(item) {
			return "<div class=''jws-rich-autocomplete-multiple''> <div class=''jws-rich-autocomplete-text'' ><label>Language Name: </label>" + item.languageName
			 + "</div> <div class=''jws-rich-autocomplete-id''><label>Language Id: </label>" + item.languageId 
			 + "<div class=''clearfix''></div> </div>";
		}
	});
});	
 </script>   
```

![](/cf/files/40289d3d765124480176512a79560002)


# Example to include basic autocomplete with clear text:

HTML Structure: 

```
		<div class="col-6">
			<div class="col-inner-form full-form-fields">
				<label for="flammableState" style="white-space:nowrap">Autocomplete(Local Storage)</label>
				<div class="search-cover">			
					<input class="form-control" id="rbAutocompleteCT" type="text">
					<i class="fa fa-search" aria-hidden="true"></i>
            	</div>
 			</div>
		</div>
```

JS changes: 
```
<script>
let autocompleteCT;

$(function () {
 	autocompleteCT = $(''#rbAutocompleteCT'').autocomplete({
        autocompleteId: "resourcesAutocomplete",
		pageSize: 10,//Default page size is 10
		prefetch : true,
		enableClearText: true,
        render: function(item) {
        	let renderStr ='''';
        	if(item.emptyMsg == undefined || item.emptyMsg === ''''){
        		renderStr = ''<p>''+item.text+''</p>'';
    		}else{
        		renderStr = item.emptyMsg;	
    		}	    				        
            return renderStr;
        },
        additionalParamaters: {languageId: 1},
        extractText: function(item) {
            return item.text;
        },
        select: function(item) {
            $("#rbAutocompleteCT").blur();
        },
		resetAutocomplete: function(){ 
        	//This function will be executed onblur or when user click on clear text button
        	//Code to reset dependent JavaScript variables, input fields etc.
        },  	
    });
});    
 </script>   
```

![](/cf/files/40289d3d765124480176512a89830003)

# Example to include basic multiselect:    
HTML Structure: 
```
<div class="col-6">
	<div class="col-inner-form full-form-fields">
		<label for="flammableState" style="white-space:nowrap">${messageSource.getMessage(''jws.multiselect'')}</label>
		<div class="search-cover">				
			<input class="form-control" id="bsMultiselect" type="text">
			<i class="fa fa-search" aria-hidden="true"></i>
        </div>	
	</div>
</div>
```
 
JS changes:
```
<script>
let basicMultiselect;
$(function () {
	basicMultiselect = $(''#bsMultiselect'').multiselect({
        autocompleteId: "resourcesAutocomplete",
        enableClearAll: true,//true enables Clear All functionality
        render: function(item) {
        	let renderStr ='''';
        	if(item.emptyMsg == undefined || item.emptyMsg === ''''){
        		renderStr = ''<p>''+item.text+''</p>'';
    		}else{
        		renderStr = item.emptyMsg;	
    		}	    				        
            return renderStr;
        },
        additionalParamaters: {languageId: 1},
        extractText: function(item) {
            return item.text;
        },
        selectedItemRender: function(item){
            return item.text;
        },
        select: function(item) {
        	$("#bsMultiselectLS").blur();
            basicMultiselect.setSelectedObject(item);
        },	
    });
</script>
```

# Example to include multiselect with localstorage:    
HTML Structure: 
```
<div class="col-6">
	<div class="col-inner-form full-form-fields">
		<label for="flammableState" style="white-space:nowrap">${messageSource.getMessage(''jws.multiselectLocalStorage'')}</label>
		<div class="search-cover">				
			<input class="form-control" id="rbMultiselectLS" type="text">
			<i class="fa fa-search" aria-hidden="true"></i>
        </div>	
	</div>
</div>
```
 
JS changes:
```
<script>
let rbMultiselectLS;
$(function () {
	rbMultiselectLS = $("#rbMultiselectLS").multiselect({
            paging:false,
            items: [{
            	key: "English",
                languageName: "English",
                languageId: 1
            }, {
            	key: "French",
                languageName: "French",
                languageId: 2
            }, {
            	key: "Hindi",
                languageName: "Hindi",
                languageId: 3
            }],

            render: function(item) {
                let renderStr ="";
                if(item.emptyMsg == undefined || item.emptyMsg === ''''){
                    renderStr = "<p>"+item.languageName+"</p>";
                }else{
                    renderStr = item.emptyMsg;    
                }                                
                return renderStr;
            },

            filter: function(items, searchTerm) {
                return items.filter(function(item) {
                	return item.languageName.toLowerCase().indexOf(searchTerm.toLowerCase()) !== -1;
                });
            },
            extractText: function(item) {
                return item.languageName;
            },

            selectedItemRender: function(item){
                return item.languageName;
            },

            select: function(item) {
                $("#rbMultiselectLS").blur();
                rbMultiselectLS.setSelectedObject(item);
            }    

        });
});
</script>
```
 
![](/cf/files/40289d3d765124480176512aa3f80005) 

# Example to include dependent multiselect component:
HTML Structure: 
```
<div class="col-6">
	<div class="col-inner-form full-form-fields">
		<label for="flammableState" style="white-space:nowrap">${messageSource.getMessage(''jws.multiselect'')}</label>
		<div class="search-cover">			
			<input class="form-control" id="rbMultiselect" type="text">
			<i class="fa fa-search" aria-hidden="true"></i>
	    </div>
    </div>
</div>	
```
JS changes:
```
<script>
let multiselect;

$(function () {
 multiselect = $(''#rbMultiselect'').multiselect({
        autocompleteId: "resourcesAutocomplete",
        render: function(item) {
        	let renderStr ='''';
        	if(item.emptyMsg == undefined || item.emptyMsg === '''')
    		{
        		renderStr = ''<p>''+item.text+''</p>'';
    		}
        	else
    		{
        		renderStr = item.emptyMsg;	
    		}	    				        
            return renderStr;
        },
        additionalParamaters: {languageId: 1},
        extractText: function(item) {
            return item.text;
        },
        selectedItemRender: function(item){
            return item.text;
        },
        select: function(item) {
        	let dependArray = new Array();
        	let dependObj = new Object();
        	dependObj.componentId = "languages";
        	dependObj.context = languageSelector;
        	dependArray.push(dependObj);
        	let dependentCompUpdated = multiselect.resetDependent(dependArray);
        	if(dependentCompUpdated === true){ 
	            multiselect.setSelectedObject(item);
	        }
	        $("#rbMultiselect").blur();
	        $("#rbMultiselect").val("");
        },	
    }, [{key: "jws.action", languageId: 1, text: "Action"}]);
});

//User can reset any multiselect component by calling resetMultiselect function
multiselect.resetMultiselect();

</script>
 ```

![](/cf/files/40289d3d765124480176512a96630004)
    

 
**Note** : - Do not include limit in autocomplete query.', 11, NOW(), 'admin@jquiver.com');

/*****************************************************Autocomplete - End*********************************************************/



/****************************************************File Bin - Start************************************************************/

REPLACE INTO jq_manual_entry (manual_entry_id, manual_type, entry_name, entry_content, sort_index, last_modified_on, last_updated_by) 
VALUES('8b425d39-72f0-4c6f-b71c-4ef247979538', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'File Bin', '# File Bin
File Bins play a pivotal role in managing the files using JQuiver application. We as a JQuiver application helps you to manage your files between different users and roles as per your requirement.

To make use of File Bins, you need to access the File Bins module and Add a new File Bin.
 ![](/cf/files/f288f186-d985-4a53-abb1-4b293ef92bf1) 
 ![](/cf/files/a318aa76-ce66-446e-8418-fdbc62ede7c8)
 
 By default JQuiver has provided two File Bins.
 1. default :- Default usage. All the Files will be uploaded on the Common Files.
 2. helpManual :- This file bins manages the files specifically for JQuiver Help Manuals.

For a File Bin there are different properties to be specified.

| Property Name | Usage |
| -------- | -------- | 
| File-bin-id     | Unique id, used to identify the file bins. This id''s cannot be duplicate and it helps you to manage the files.     |
| File types supported     |Different types of file to be supported in that file bin. Providing specific extensions, helps you to add restrictions on the upload of file. If no restrictions has to be made, then you can add *     |
| Max file size      | This is the size of file which is to be uploaded. JQuiver has provided the conversion of file sizes between, bytes, kilo bytes and mega bytes. As per users understanding and comfortability, size can be provided. |
| No of files     | This will help to manage and provide a restriction for the user on file upload count.     |

Apart from the above properties, user can provide specific restrictions on select, upload, view and delete of files in a file bin. To provide these restrictions, JQuiver has provided specific SQL terminals for all the four features, in which you can provide 
By default, all the SQL terminals are off, which means, no restrictions will be there for any of the operations on the files uploaded in that particular file bin.

If user wants to provide restriction on any of the functionality or all of the feature, then these SQL terminal has to be turned on and query has to be written as per users requirement.

Below we will take an example and understand each of the feature SQL.

1. **Select**
Here you have to write a Select query, which will allow you or your application to view the files in the File Bin,as per your requirement.
The query should return "fileAssociationId", "fileBinId", "filePath", "fileUploadId", "originalFileName" and "physicalFileName", as alias, which is mandatory.
Below snippet, shows and example where user can view files uploaded only by themselves and admin can view all the files uploaded.
 ![](/cf/files/8c901fa8-4c71-48dd-bd1c-f2e6bdba2fce)
 
 As per above select query, below as the files being visible to Admin, Authenticated user and Anonymous user
 For Anonymous, only files uploaded by all the anonymous users will be visible. Here we have uploaded, request_issue.png, by Anonymous User.
 ![](/cf/files/03f110de-811d-47f3-be2e-fc6196c11ace)
 
 For Authenticated user, only files uploaded by themselved will be visible. Here we have uploaded, MicrosoftTeams-image (3).png, by an Authenticated user (mini.pillai@trigyn.com)
 ![](/cf/files/c57e84f0-aa52-4597-98d8-9eb2afd90cc5)
 For Another authenticated user, no files will be visible, as he/she has not uploaded anything.
 ![](/cf/files/b71984b5-841b-4b6e-bffa-77ca956105dc)
 
 For Admin, all the 3 files will be visible
 ![](/cf/files/74909e25-3efa-41c7-a11f-8e02dcc1768c)
		
2. **Upload**
As explained in Select Query, We can provide restrictions for uploading a file as well. By default there wont be any restrictions. If we turn on this sql terminal, then a default query will be generated. You can update the query as per your requirement.
The query should return data with alias "isAllowed". If value of isAllowed is 1, then upload will be allowed. If it is 0, then upload will be restricted.
Below is an example, if uploading files only for authenticated user. Anonymous user will not be able to upload the file.
![](/cf/files/62765d11-c5ad-4128-a94a-f55ac5b36bbe)

Now if an Authenticated user tries to upload a file in the file bin, it will get successfully uploaded. Here we are uploading file service_request(1).svg
![](/cf/files/6908081e-3441-40f0-87db-3e55f00feb83)

And if an anonymous user tries to upload the file, it will give permission denied and wont allow to upload.
![](/cf/files/6d2b0993-be4c-43c8-a72f-19eaf5a6ee93)

3. **View**
As explained in Select Query, We can provide restrictions for downloading a file uploaded as well. By default there wont be any restrictions. If we turn on this sql terminal, then a default query will be generated. You can update the query as per your requirement.
The query should return data with alias "isAllowed". If value of isAllowed is 1, then view/download will be allowed. If it is 0, then view/download will be restricted.
 ![](/cf/files/8606b360-59e0-469f-86ea-737605981c04)

4. **Delete**      
Using this, restriction criteria can be provided for deleting a file. By default there wont be any restrictions. If we turn on this sql terminal, then a default query will be generated. You can update the query as per your requirement. 
The query should return data with alias "isAllowed". If value of isAllowed is 1, then delete will be allowed. If it is 0, then delete will be restricted.
![](/cf/files/05777b2b-c424-4472-8678-363d4fe3ac5d)
In the above example of delete query, we are providing restriction on deleting files as per owner. That is, user can delete files, uploaded by them. They wont have rights to delete files uploaded by any other user. Whereas, Admin will have right to delete all the files, irrespective of the file owner.


Steps to initialize and use file component in your template or form:


**Step 1: ** Include following CSS and JS files in your template:
```
    <link rel="stylesheet" type="text/css" href="${(contextPath)!''''}/webjars/1.0/dropzone/dist/dropzone.css" />
    <script type="text/javascript" src="${(contextPath)!''''}/webjars/1.0/dropzone/dist/dropzone.js"></script>
    <script type="text/javascript" src="${(contextPath)!''''}/webjars/1.0/fileupload/fileupload.js"></script>
```

**Step 2: ** Create HTML element
```
	<div class="col-3">
		<div class="col-inner-form full-form-fields">
			<label for="reimbursementFiles" style="white-space:nowrap"><span class="asteriskmark">*</span>
				Attach Files
			</label>
			<div class="reimbursementFiles col-12 dropzone"></div>
		</div>
	</div>
```							
							
**Step 3: **  While initializing file component, use file Bin Id as **fileBinId**. If there are no configuration found for provided id then system will use **default ** configuration id.
If createFileBin is true then only file component will be created otherwise file component will not be created but you can use other function like getSelectedFileIds to get fileUploadId of uploaded files.

```

    let reimbursementFiles = $(".reimbursementFiles").fileUpload({
        fileBinId : "default",
        fileAssociationId: "fileAssociationId",
        createFileBin : true,
        successcallback: fileUploadSuccess.bind(this),
        deletecallback: fileDeleteSuccess.bind(this)
    });
```

Note: Initialize file bin component before ready function. 

**Step 4: ** Create success and delete callback function if required
```

    function fileUploadSuccess(fileId){
		// code changes
    }

    function fileDeleteSuccess(deletedFileUploadId){
    	// code changes
    }
```

Function available for all file bin components:
* getSelectedFileIds - List of uploaded file id
* deleteFileFromServerById - Delete file from the server
* viewFileEvent - View Uploaded File

 
', 12, NOW(), 'admin@jquiver.com');

/*****************************************************File Bin - End*************************************************************/



/****************************************************Dashboard - Start***********************************************************/

REPLACE INTO jq_manual_entry (manual_entry_id, manual_type, entry_name, entry_content, sort_index, last_modified_on, last_updated_by) VALUES
('3f0f6b4e-9a00-4b89-9a64-415a1f8256d2', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'Dashboard', '# Dashboard

In order to import the dashboard jar, add following dependency in your pom.xml.

```
<dependency>
	<groupId>com.trigyn</groupId>
	<artifactId>dashboard</artifactId>
	<version>latest-version</version>
</dependency>
```

This will import dashboard jar implicitly and will create dashboard table.The jar will provide an URL server name:port/cf/dls which will render selected dashboard. Creating a dashboard is an easy task. Just provide dashboard name, select context name and based on context name select dashlets to be displayed on dashboard.

# Dashlet
Dashlet component is included in dashboard jar itself. User need to provide following basic properties for a dashlet:
* Dashlet name
* Dashlet title
* Dashlet coordinates
* Dashlet dimesions
* Context type 
* Dashlet status.



User can also add provide additional configurable properties to a dashlet like no of records to be visible at a time or you can allow end user to enter a date which will be taken into consideration while fetching records. User also need to provide HTML content and SQL query.

**Step 1: **Navigate to Dashboard Master.
![](/cf/files/402816947628e22d017628e709eb0000)

**Step 2: **Click Manage Dashlets
 ![](/cf/files/40289d3d7650c0a1017650c8fccf0001)
 
**Step 3: **Click on Create New Dashlet
  ![](/cf/files/40289d3d7650c0a1017650c90cce0002)
	
**Step 4: **Create New Dashlet by providing all neccessary information
![](/cf/files/402816947628e22d017628e71f020001)
![](/cf/files/402816947628e22d017628ea39e00002)

**Step 5: **Navigate back to Dashboard listing page and click on Create New Dashboard
 ![](/cf/files/40289d3d7650c0a1017650c8ef410000)

**Step 6: **Create new dashboard by providing all neccessary information
![](/cf/files/40289d3d7629c275017629c35c830000)



', 13, NOW(), 'admin@jquiver.com');

/*****************************************************Dashboard - End************************************************************/



/****************************************************Versioning - Start**********************************************************/

REPLACE INTO jq_manual_entry (manual_entry_id, manual_type, entry_name, entry_content, sort_index, last_modified_on, last_updated_by)
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
![](/cf/files/40289d3d7629c27501762a0eb728000a) 


**Step 2: ** Select revision time using autocomplete:
![](/cf/files/40289d3d7629c27501762a0ee8f3000b)


**Step 3: ** See the difference between two versions
![](/cf/files/40289d3d76509b1e017650ae7d4d0015)


**Step 4: ** If you want you can replace current data with old data using copy button:
![](/cf/files/40289d3d7629c27501762a0f3e5a000c)


**Complex example:**

![](/cf/files/40289d3d7629c27501762a0f8466000d)
 
![](/cf/files/40289d3d7629c27501762a0f914b000e)

![](/cf/files/40289d3d7629c27501762a0f9c51000f)', 14, NOW(), 'admin@jquiver.com');

/*****************************************************Versioning - End***********************************************************/




/************************************************Import Configuration - Start***************************************************/

REPLACE INTO jq_manual_entry (manual_entry_id, manual_type, entry_name, entry_content, sort_index, last_modified_on, last_updated_by)
VALUES('be37c240-2607-4d79-9ef1-136dbd7c524b', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'Import Configuration', '# Import Configuration

Importing the configuration plays pivotal role when you want to migrate some configuration or data from an existing database to another database. 

To import a configuration browse and upload the particular zip file already exported and saved in the local drive. Once uploaded, all the configuration to be imported will be listed down with the existing version number and import version number. Existing version number is the number which is available in your current setup, if the configuration already exists and import version number will be the number of the configuration from the setup from which it was exported.
The configuration can imported all together or can be imported one by one after review, by the user. This can also be compared, with the existing version in the system, if any, using the comparison icon (if enabled).
![](/cf/files/40288089766b4ac701766b5f263d0017)

Comparison of the imported configuration is done and can be viewed as in versioning module.

 ![](/cf/files/40288089766b4ac701766b61fa4a0019)

**For Developer:** 
To implement import of any new configuration, other than what is currently available, following changes has to be taken care.

 **Step 1: ** Create a XML VO/POJO to export the entity to be exported in XML format.
 
  ```
@XmlRootElement(name = "data")
@XmlAccessorType (XmlAccessType.FIELD)
public class ModuleXMLVO extends XMLVO {
	
	@XmlElement(name = "object")
    private List<entity-name> details = new ArrayList<>();

	public List<entity-name> getDetails() {
		return details;
	}

	public void setDetails(List<entity-name> details) {
		this.details = details;
	}
}
 ```
 
 **Step 2: ** Create a Json VO/POJO if required.
 This VO will be a normal java class with private variables and its getters and setters. This class will be used for comparison. Hence only those variables will be maintained in this class, which should be available in the versioning/comparison UI.
 
 **Step 3: ** 
Do appropriate changes in ImportService, to handle the import of new module added. i.e;
1. Convert the XML VO into appropriate entity object, by un-marshaling.
2. Convert the entity object to Json VO/POJO.
3. Invoke "updateOutputMap()", with required inputs.
4. Handle appropriate save logic, using repository.save(), for the newly added module.

Please note, following has to be  kept in mind while handling import service.
1. The key in the output map will be the combination of moduleID and moduleType. This is because, there can be case where the module id can repeat in different modules. Hence this will help in maintaining the uniqueness.
2. If at all, there is any primary key with UUID generator, then make sure to update the column with ``` inquisitive-uuid```, if not available.
 ```
 @Entity
@Table(name = "jq_user")
public class JwsUser {

	@Id
	@GeneratedValue(generator = "inquisitive-uuid")
	@GenericGenerator(name = "inquisitive-uuid", strategy = "com.trigyn.jws.dbutils.configurations.CustomUUIDGenerator")
	@Column(name="user_id")
	private String userId = null;
	
	@Column(name="first_name")
	private String firstName = null;
	
 ```
This will help to maintain the same primary, while importing, if the configuration is not available in the system.

**Step 4:** 
Once the server side code change is done, make appropriate changes in import.js. For this, add the appropriate condition in ```submitRevisionForm(moduleType, entityId)``` method. For this following is taken into consideration.
1. Provide saveURL if it is not a dynamic form module.
2. Provide formId, if it is a dynamic form module.
3. If it is a non versioning module. That is, if the versioning feature is not available for this module then
		a. Provide isNonVersioningModule = "true"
		b. Provide nonVersioningFetchURL
		c. Provide saveURL
4. Also, update server side code to satisfy the  nonVersioningFetchURL, saveURL request, if newly added module is a non versioning module.


', 15, NOW(), 'admin@jquiver.com');

/************************************************Import Configuration - End*****************************************************/




/************************************************Export Configuration - Start***************************************************/

REPLACE INTO jq_manual_entry (manual_entry_id, manual_type, entry_name, entry_content, sort_index, last_modified_on, last_updated_by)
VALUES('dd97c23d-feef-4cea-afcf-3cece7819159', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'Export Configuration', '# Export Configuration

Exporting the configuration plays pivotal role when you want to migrate some configuration or data from an existing database to another database. 

To export a configuration go to particular module on the export configuration page and select the same. By default all the custom configurations will be selected and system configurations deselected. If at all user wants to reset the default selection and export only particular configuration, then that can be achieved by "Deselect All" checkbox.

![](/cf/files/40288089766a9a9501766acfa31b0002)

Once the configuration to be exported are finalized, go to the preview page using "Next" button and get it exported in zip format.

 ![](/cf/files/40288089766a9a9501766acfe7920003)

**For Developer:** 
To implement export of any new configuration, other than what is currently available, following changes has to be taken care.

**Step 1: ** Add the menu for new module to be introduced.

This requires database change.
Add module_name, grid_details_id and module_type(this will be unique string, which will be used to identify the module throughout the export process) into jq_master_module table.

![](/cf/files/40288089766a9a9501766ad0ba300004)
 
 **Step 2: ** Create a XML VO/POJO to export the entity to be exported in XML format.
 
  ```
@XmlRootElement(name = "data")
@XmlAccessorType (XmlAccessType.FIELD)
public class ModuleXMLVO extends XMLVO {
	
	@XmlElement(name = "object")
    private List<entity-name> details = new ArrayList<>();

	public List<entity-name> getDetails() {
		return details;
	}

	public void setDetails(List<entity-name> details) {
		this.details = details;
	}
}
 ```
 
 **Step 3: ** Add getObject() method in the particular entity, if does not exist, to return a clone of the entity object.
 
 ```
 public Autocomplete getObject() {
		Autocomplete autocomplete = new Autocomplete();
		
		autocomplete.setAcTypeId(acTypeId);
		autocomplete.setAutocompleteDesc(autocompleteDesc!=null?autocompleteDesc.trim():autocompleteDesc);
		autocomplete.setAutocompleteId(autocompleteId);
		if(autocompleteSelectQuery!=null) {
			autocomplete.setAutocompleteSelectQuery(autocompleteSelectQuery.trim());
		} else {
			autocomplete.setAutocompleteSelectQuery(autocompleteSelectQuery);
		}
		autocomplete.setAutocompleteSelectQuery(autocompleteSelectQuery);
		autocomplete.setAcTypeId(this.acTypeId);
		return autocomplete;
	}
 ```
 
**Step 4: ** 
Do appropriate changes in ExportService, to handle the export of new module added.
Please make a note that, while retrieving data from database, we are fetching all the system configuration selected in the UI and excluding all the custom configuration included in the list passed from the client.

**Step 5:** 
Once the server side code change is done, make appropriate changes in export.js. For this, add the tab UI appropriately in ```openTab(evt, gridID, moduleType)``` method. 
Make sure to manage the condition for listing to UI, as per the module_type value specified in jq_master_module table.
Currently we have handled only grid listing in export configuration page.', 

16, NOW(), 'admin@jquiver.com');

/************************************************Export Configuration - End*****************************************************/




/************************************************Dev Environment - Start********************************************************/

REPLACE INTO jq_manual_entry (manual_entry_id, manual_type, entry_name, entry_content, sort_index, last_modified_on, last_updated_by) 
VALUES('935b9394-c33d-4113-a248-27c46c45e7e9', '07cf45ae-2987-11eb-a9be-e454e805e22f', 'Dev Environment', '# Dev Environment:

You can modify the following modules in your favourite editor just by changing the value of **Profile** property to **dev** from application configuration:
* Templating
* Dynamic Form
* Dashlet

![](/cf/files/40289d3d765d4f9c01765d5514740000)

You can configure default path which will be used by the application to download, upload and process the content

![](/cf/files/4028168b7647e50a017648ca0c690009)

Ddev environment features:
** Download: **If you want to download(from database to local directory) all custom records then you can use Download All feature.
** Upload:**If you want to upload(from local directory to database) all custom records then you can use Upload All feature.
** Note: **If you add any new record from the UI then application will save the changes in database as well as create appropriate files in local directory.
 
Download All custom templates:
![](/cf/files/40289d3d765d4f9c01765d67c7d90005) 
	
Download All custom dynamic forms:
![](/cf/files/40289d3d765d4f9c01765d67e8320007)

Download All custom dashlets:
![](/cf/files/40289d3d765d4f9c01765d7279a50009)


Upload All custom templates:
![](/cf/files/40289d3d765d4f9c01765d67d7850006)
 
Upload All custom dynamic forms:
![](/cf/files/40289d3d765d4f9c01765d67f5d90008)
 
Upload All custom dashlets:
![](/cf/files/40289d3d765d4f9c01765d7287ee000a)


Download/Upload system templates
![](/cf/files/40289d3d7660efd0017660f91702000a)

Download/Upload system dynamic forms
![](/cf/files/40289d3d7660efd0017660f9ba53000c)

Download/Upload system dashlets
![](/cf/files/40289d3d7660efd0017660f9a518000b) 


Find all the templates under **Templates** folder. It consist of **template-name.tgn** files

![](/cf/files/40289d3d7661398f0176615d9dbd0001)
 
 Application will save all forms in **DynamicForms** folder. Inside DynamicForms, there will be one folder for each dynamic form and it should contain at least 3 files:
*  htmlContent
*  selectQuery
*  saveQuery-1

![](/cf/files/402816927661a061017661a86f8c0004)

![](/cf/files/40289d3d7661398f0176617633730002)



**Note: **There should be one file for each save query.

Like dynamic forms, each dashlet details will be stored in seperate folder and all dashlets will be saved in Dashlets folder. There should be only two files in each folder:
* htmlContent
* selectQuery

![](/cf/files/40289d3d7661398f0176617e3aee0004)

![](/cf/files/402816927661a061017661a88a860006)


', 17, NOW(), 'admin@jquiver.com');

/************************************************Dev Environment - End**********************************************************/



/*************************************************Others - Start****************************************************************/
REPLACE INTO jq_manual_entry (manual_entry_id, manual_type, entry_name, entry_content, sort_index, last_modified_on, last_updated_by) 
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
<#noparse>
```
<script type="text/javascript" src="${contextPath!''''}/webjars/1.0/JSCal2/js/jscal2.js"></script>
<script type="text/javascript" src="${contextPath!''''}/webjars/1.0/JSCal2/js/lang/en.js"></script>
```
</#noparse>

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

', 18, NOW(), 'admin@jquiver.com');

/*************************************************Others - Start****************************************************************/



/*************************************************Help Manual - Start***********************************************************/

REPLACE INTO jq_manual_entry (manual_entry_id, manual_type, entry_name, entry_content, sort_index, last_modified_on, last_updated_by) 
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
 
 Your manual can be viewed either from "Help Manual" module, or you can provide an entry in SIte Layout to accesss you manual from menu or any other location in your application.
 
 **JQuiver Developer Guide**
 
 While adding new manual, following entries in database will get modified.
 A new entry in jq_manual_type is created.
 Then as per the new manual entries, data is updated in jq_manual_entries. A foreign key constarint will be there in this table, with the manual_id in jq_manual_type.
 If any file is uploaded in the manual entry, then data entry will be made in jq_file_upload.
 In jq_file_upload, file_association_id will be the manual_entry_id from jq_manual_entries. Other than that, it will have the file_bin_id which is from jq_file_upload_config table, then the location where the image is available. The file location can be configured in Application Configuration module.
 Apart from this, it will also have the physical_file_name, which will be the name in which the file will be available in the specified location. ',
  19, NOW(), 'admin@jquiver.com');


/*************************************************Help Manual - End*************************************************************/


REPLACE INTO jq_file_upload (file_upload_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_bin_id, file_association_id) VALUES
('40281694762864a50176288c66620001', '/images', 'REST_API_Master.PNG', '54d81128-66eb-44e1-bd7c-a7aba6c1f081', 'admin@jquiver.com', NOW(), 'helpManual', '81c506ff-dab5-43de-a790-58af356de3e9'), 
('40281694762864a50176288c755a0002', '/images', 'Add_Edit_REST_API_Step_1.PNG', 'f34047da-dbe7-4f0a-8672-8ff74a47b391', 'admin@jquiver.com', NOW(), 'helpManual', '81c506ff-dab5-43de-a790-58af356de3e9'),
('402816947628cb18017628cfa6110000', '/images', 'Add_Edit_REST_API_Step_2.PNG', '538e5a16-0eb6-47d3-b196-23733426a0e1', 'admin@jquiver.com',  NOW(), 'helpManual', '81c506ff-dab5-43de-a790-58af356de3e9'),

('402816947628e22d017628e709eb0000', '/images', 'Dashboard_Master.PNG', '1a999d28-8f4b-4475-99e6-439e7b40bec4', 'admin@jquiver.com',  NOW(), 'helpManual', '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'), 
('40289d3d7650c0a1017650c8fccf0001', '/images', 'Add_Edit_Dashlet_Step_1.PNG', '5eda819c-bcbb-486a-b94b-1d93d73379c5', 'admin@jquiver.com',  NOW(), 'helpManual', '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'), 
('40289d3d7650c0a1017650c90cce0002', '/images', 'Add_Edit_Dashlet_Step_2.PNG', 'e54568fc-8d7c-40eb-ab62-cd8521956878', 'admin@jquiver.com',  NOW(), 'helpManual', '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'),
('402816947628e22d017628e71f020001', '/images', 'Add_Edit_Dashlet_Step_1.PNG', 'ff8c8d67-2b68-4a2b-843b-de7d747324b7', 'admin@jquiver.com',  NOW(), 'helpManual', '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'), 
('402816947628e22d017628ea39e00002', '/images', 'Add_Edit_Dashlet_Step_2.PNG', '75368f52-d90f-456c-b9f5-69703014991e', 'admin@jquiver.com', NOW(), 'helpManual', '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'),
('40289d3d7650c0a1017650c8ef410000', '/images', 'Add_Edit_Dashboard_Step_1.PNG', '8346ff39-89b4-40f2-b1fa-cd01ca503541', 'admin@jquiver.com', NOW(), 'helpManual', '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'),
('40289d3d7629c275017629c35c830000', '/images', 'Add_Edit_Dashboard_Step_2.PNG', '606e1003-2f89-4514-9b63-1ff0d11ac8ce', 'admin@jquiver.com', NOW(), 'helpManual', '3f0f6b4e-9a00-4b89-9a64-415a1f8256d2'),

('40289d3d7629c27501762a0eb728000a', '/images', 'Module_Revision_Enable_UI.PNG', '461685d6-a188-406c-920e-41bc210ada68', 'admin@jquiver.com', NOW(), 'helpManual', '1d5577fa-a7e3-4bdf-85ed-b84ac81fbb0b'), 
('40289d3d7629c27501762a0ee8f3000b', '/images', 'Module_Revision_Simple_Example_1.PNG', '5303697a-2fc2-4abc-af89-9621fe0fdf25', 'admin@jquiver.com', NOW(), 'helpManual', '1d5577fa-a7e3-4bdf-85ed-b84ac81fbb0b'), 
('40289d3d76509b1e017650ae7d4d0015', '/images', 'Module_Revision_Simple_Example_2.PNG', 'af8ce88f-a5f0-401d-ac19-05fb1e583d15', 'admin@jquiver.com', NOW(), 'helpManual', '1d5577fa-a7e3-4bdf-85ed-b84ac81fbb0b'),
('40289d3d7629c27501762a0f3e5a000c', '/images', 'Module_Revision_Simple_Example_3.PNG', 'ed8c766f-5da2-4dbe-a335-c3aeca501157', 'admin@jquiver.com', NOW(), 'helpManual', '1d5577fa-a7e3-4bdf-85ed-b84ac81fbb0b'), 
('40289d3d7629c27501762a0f8466000d', '/images', 'Module_Revision_Complex_Example_1.PNG', '4713e718-d56d-46f4-9d98-fb0c74902c4a', 'admin@jquiver.com', NOW(), 'helpManual', '1d5577fa-a7e3-4bdf-85ed-b84ac81fbb0b'), 
('40289d3d7629c27501762a0f914b000e', '/images', 'Module_Revision_Complex_Example_2.PNG', '44c79c88-138d-487e-ab18-187c55efff6c', 'admin@jquiver.com', NOW(), 'helpManual', '1d5577fa-a7e3-4bdf-85ed-b84ac81fbb0b'),
('40289d3d7629c27501762a0f9c51000f', '/images', 'Module_Revision_Complex_Example_3.PNG', '1de987bc-dbc4-46f3-9eb4-d524304c5fe4', 'admin@jquiver.com', NOW(), 'helpManual', '1d5577fa-a7e3-4bdf-85ed-b84ac81fbb0b'),

('40289d3d7629c27501762a22257f0012', '/images', 'Grid_Util_Master.PNG', '5a25ffc8-d516-41ca-931e-6a3ad8ddf6bf', 'admin@jquiver.com', NOW(), 'helpManual', '7428a452-da97-4ef0-b6d7-acf4921beb82'), 
('40289d3d7629c27501762a22341b0013', '/images', 'Add_Grid_Details_Step_1.PNG', 'dbbaf3ee-eb76-4e9c-8df4-27e8be86cab2', 'admin@jquiver.com', NOW(), 'helpManual', '7428a452-da97-4ef0-b6d7-acf4921beb82'), 
('40289d3d7629c27501762a223f2e0014', '/images', 'Add_Grid_Details_Step_2.PNG', '2cb34a18-ad8c-4ace-818f-d7d3eb0d5d5d', 'admin@jquiver.com', NOW(), 'helpManual', '7428a452-da97-4ef0-b6d7-acf4921beb82'), 
('40289d3d7629c27501762a2258450015', '/images', 'Default_Listing_Template.PNG', '165dad67-09bf-4521-b4cf-023f7851516f', 'admin@jquiver.com', NOW(), 'helpManual', '7428a452-da97-4ef0-b6d7-acf4921beb82'),

('40289d3d762c323c01762cc6c8a30000', '/images', 'Multilingual_Master.PNG', '263dbcbd-754e-42d0-b1bf-3ba1dfa85a0e', 'admin@jquiver.com', NOW(), 'helpManual', '9c25fb63-8336-4f22-bb97-a5042159d5c4'), 
('40289d3d762c323c01762cc6f25c0001', '/images', 'Add_Edit_Multilingual_Data_Step_1.PNG', 'd6e30474-ca6c-4b3c-8894-090f1d985de5', 'admin@jquiver.com', NOW(), 'helpManual', '9c25fb63-8336-4f22-bb97-a5042159d5c4'), 
('40289d3d762c323c01762cc7015f0002', '/images', 'Add_Edit_Multilingual_Data_Step_2.PNG', '3d331031-fb3c-4aaa-81f9-e18dc99d2812', 'admin@jquiver.com', NOW(), 'helpManual', '9c25fb63-8336-4f22-bb97-a5042159d5c4'), 
('40289d3d762c323c01762cc712310003', '/images', 'Multilingual_Demo.PNG', '5a1a9bf9-ce85-483c-8eb9-a9ce0d8884b9', 'admin@jquiver.com',NOW(), 'helpManual', '9c25fb63-8336-4f22-bb97-a5042159d5c4'), 
('40289d3d762c323c01762cc71ee00004', '/images', 'Multilingual_Demo_French.PNG', '688edc01-ec45-4a09-a08e-d18f7b5000ee', 'admin@jquiver.com', NOW(), 'helpManual', '9c25fb63-8336-4f22-bb97-a5042159d5c4'), 
('40289d3d762c323c01762cc72f480005', '/images', 'Multilingual_Demo_Hindi.PNG', '86247f52-2f06-4506-b8e5-03056d3bbaca', 'admin@jquiver.com', NOW(), 'helpManual', '9c25fb63-8336-4f22-bb97-a5042159d5c4'),
('40289d3d763887410176388995320000', '/images', 'Google_Analytics_Application_Configuration.PNG', '2e4403fd-9d8f-42ed-a782-551e45f50a59', 'admin@jquiver.com', NOW(), 'helpManual', '918676c8-b653-43ee-964a-d4faaeb13787'), 
('40289d3d763887410176388b36b60001', '/images', 'Dev_Environment_Application_Configuration.PNG', '11fdbaad-55f3-48e6-8032-4fd650df0e30', 'admin@jquiver.com', NOW(), 'helpManual', '918676c8-b653-43ee-964a-d4faaeb13787'),
('4028168b7647e50a0176488ec8860003', '/images', 'Template_Without_Param.PNG', 'c9336bc5-f083-42cd-bf70-152ef859b638', 'admin@jquiver.com', NOW(), 'helpManual', '17feffba-99f4-4591-9cb5-0fef46ee0b77'), 
('4028168b7647e50a0176488f32110004', '/images', 'Template_With_Param.PNG', '3fb18376-b4df-43c7-bfde-a62ac98e241b', 'admin@jquiver.com', NOW(), 'helpManual', '17feffba-99f4-4591-9cb5-0fef46ee0b77'), 
('4028168b7647e50a0176488f4ebe0005', '/images', 'Resource_Bundle_Without_Default.PNG', 'db085e59-de62-440c-a8c5-039595a8392a', 'admin@jquiver.com', NOW(), 'helpManual', '17feffba-99f4-4591-9cb5-0fef46ee0b77'), 
('4028168b7647e50a0176488f60ba0006', '/images', 'Resource_Bundle_With_Default.PNG', '79fd4660-5fd7-40c0-9238-92a0b9e91cae', 'admin@jquiver.com',NOW(), 'helpManual', '17feffba-99f4-4591-9cb5-0fef46ee0b77'),

('4028168b7647e50a017648c9a2fc0008', '/images', 'Profile_Property.PNG', '284056ff-151a-4230-a044-cf04306b89bf', 'admin@jquiver.com', NOW(), 'helpManual', 'helpManual'), 

('4028168b7647e50a017648ca0c690009', '/images', 'Template_Storage_Location_Property.PNG', '425284ef-03b1-4fbd-81f2-6649c6fb0404', 'admin@jquiver.com', NOW(), 'helpManual', '935b9394-c33d-4113-a248-27c46c45e7e9'),
('40289d3d765d4f9c01765d67c7d90005', '/images', 'Template_Download_All.PNG', '118e8bd6-1fa5-4b72-b30a-64e4ac09a447', 'admin@jquiver.com', NOW(), 'helpManual', '935b9394-c33d-4113-a248-27c46c45e7e9'), 
('40289d3d765d4f9c01765d67d7850006', '/images', 'Template_Upload_All.PNG', '27d6eba0-55b4-4e82-82fb-4c16d38d1d19', 'admin@jquiver.com', NOW(), 'helpManual', '935b9394-c33d-4113-a248-27c46c45e7e9'), 
('40289d3d765d4f9c01765d67e8320007', '/images', 'Dynamic_Form_Download_All.PNG', '2684d1d6-6650-4d08-bed8-fb229afa969c', 'admin@jquiver.com', NOW(), 'helpManual', '935b9394-c33d-4113-a248-27c46c45e7e9'), 
('40289d3d765d4f9c01765d67f5d90008', '/images', 'Dynamic_Form_Upload_All.PNG', '58bd210e-47d5-4516-a9dc-89b27d32d49d', 'admin@jquiver.com', NOW(), 'helpManual', '935b9394-c33d-4113-a248-27c46c45e7e9'), 
('40289d3d765d4f9c01765d7279a50009', '/images', 'Dashlet_Download_All.PNG', '8090ddee-8014-46a0-b147-6c2ed5fd6d57', 'admin@jquiver.com', NOW(), 'helpManual', '935b9394-c33d-4113-a248-27c46c45e7e9'), 
('40289d3d765d4f9c01765d7287ee000a', '/images', 'Dashlet_Upload_All.PNG', '092a5276-c6c6-4710-a50b-c1913b8c2d36', 'admin@jquiver.com', NOW(), 'helpManual', '935b9394-c33d-4113-a248-27c46c45e7e9'),
('40289d3d7660efd0017660f91702000a', '/images', 'Template_Action_Buttons.PNG', '65b32265-e86e-4e87-9901-2e986dda2dc1', 'admin@jquiver.com', NOW(), 'helpManual', '935b9394-c33d-4113-a248-27c46c45e7e9'), 
('40289d3d7660efd0017660f9a518000b', '/images', 'Dashlet_Action_Buttons.PNG', '4a848d47-474d-4d27-a597-adf003fd094a', 'admin@jquiver.com', NOW(), 'helpManual', '935b9394-c33d-4113-a248-27c46c45e7e9'), 
('40289d3d7660efd0017660f9ba53000c', '/images', 'Dynamic_Form_Action_Buttons.PNG', '46d81eed-e1a6-4db2-b02f-bb617321f08f', 'admin@jquiver.com', NOW(), 'helpManual', '935b9394-c33d-4113-a248-27c46c45e7e9'),
('40289d3d7661398f0176615d9dbd0001', '/images', 'Template_Folder_Structure.PNG', 'fa1b8ef9-5a89-49b1-935d-25959e47ed21', 'admin@jquiver.com', NOW(), 'helpManual', '935b9394-c33d-4113-a248-27c46c45e7e9'),
('402816927661a061017661a88a860006', '/images', 'Dashlet_Folder_Structure_1.PNG', '2b9debb5-9f13-40e6-9d58-e51a7c83e0d7', 'admin@jquiver.com', NOW(), 'helpManual', '935b9394-c33d-4113-a248-27c46c45e7e9'),
('40289d3d7661398f0176617e3aee0004', '/images', 'Dashlet_Folder_Structure_2.PNG', '850316b0-bd29-41a3-88b8-30764903b73b', 'admin@jquiver.com',  NOW(), 'helpManual', '935b9394-c33d-4113-a248-27c46c45e7e9'),
('402816927661a061017661a86f8c0004', '/images', 'Dynamic_Form_Folder_Structure_1.PNG', 'a0661c6d-887b-44eb-9bb4-ac29cfe78dc3', 'admin@jquiver.com', NOW(), 'helpManual', '935b9394-c33d-4113-a248-27c46c45e7e9'),
('40289d3d7661398f0176617633730002', '/images', 'Dynamic_Form_Folder_Structure_2.PNG', 'b7568e9a-c31b-4492-bea1-bfe580a580ec', 'admin@jquiver.com',  NOW(), 'helpManual', '935b9394-c33d-4113-a248-27c46c45e7e9'),
('40289d3d765d4f9c01765d5514740000', '/images', 'Profile_Property.PNG', 'e8ba205c-1f99-46e6-891f-cf7f220daf76', 'admin@jquiver.com', NOW(), 'helpManual', '935b9394-c33d-4113-a248-27c46c45e7e9'),

('4028b8817646ed03017647dc3ab40021', '/images', 'manageRole.PNG', '1eab0154-70c2-4b19-824b-3a99a7f72bb5', 'admin@jquiver.com', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'), 
('4028b8817646ed03017647ef2f050022', '/images', 'manageUser.PNG', '55072ccf-76cf-4a20-b66b-8d9ba9371e38', 'admin@jquiver.com', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'), 
('4028b8817646ed03017647f1c1330023', '/images', 'forcePasswordMail.PNG', '076e8dd4-6389-4a13-be07-c524e96dbef7', 'admin@jquiver.com', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'), 
('4028b8817646ed030176480a3c080024', '/images', 'manageRoleModule.PNG', '50b32b99-29e1-45e4-8920-84701895e783', 'admin@jquiver.com', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'), 
('4028b8817646ed030176480ce3fd0025', '/images', 'manageEntityRole.PNG', '891bd5e4-043a-41eb-a623-95856f23c453', 'admin@jquiver.com', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'), 
('4028b8817646ed030176480e43ab0026', '/images', 'userManagement.PNG', '75354d31-d32b-41d8-877c-f15b86bac8da', 'admin@jquiver.com', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('4028b8817650f1220176511211c40002', '/images', 'authTypes.png', '85b8a915-bd77-4b54-9e39-0fcbc27fb9d0', 'admin@jquiver.com', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'), 
('4028b8817650f12201765116077e0003', '/images', 'databaseAuth-1.png', 'ebfd663a-42ec-4ce9-9190-f0f9b6acba74', 'admin@jquiver.com', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'), 
('4028b8817650f122017651166e0d0004', '/images', 'databaseAuth-2.png', '9563f6a8-7ef5-4bef-8db5-2a89edf07c75', 'admin@jquiver.com', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'), 
('4028b8817650f12201765121c1300005', '/images', 'databaseAuth-password.png', '7f733b86-f55b-4eb5-ab5d-1a0f530aeeec', 'admin@jquiver.com', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('4028b8817650f122017651226ab90006', '/images', 'databaseAuth-password-captcha.png', '43966179-960e-4339-aa47-94d676a44aa0', 'admin@jquiver.com', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'), 
('4028b8817650f12201765122f1750007', '/images', 'databaseAuth-totp.png', '93c8b0c5-3e68-444b-8c64-fb86c6aaf62f', 'admin@jquiver.com',NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('4028b881765fd657017660116dac0000', '/images', 'oauth-clients.png', '394f9c18-0b9e-4f05-ac14-ca4a754fc018', 'admin@jquiver.com', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('4028b881765fd65701766028c6f60001', '/images', 'google-credentials.PNG', 'e6599f51-6988-4ec3-b382-a0caeb78105a', 'admin@jquiver.com', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('4028b881765fd6570176602a9c080002', '/images', 'google-create-project.PNG', '3df83556-44da-48a1-a68b-7ad6d2d84b14', 'admin@jquiver.com', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('4028b881765fd6570176602ca2d50003', '/images', 'google-configure-consent-screen.PNG', '554929bc-c01a-4f7b-8786-386107c7e682', 'admin@jquiver.com',NOW() , 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('4028b881765fd6570176602d10ac0004', '/images', 'google-configure-consent-screen-2.PNG', '86102523-6b13-4313-8d27-c9db1816d54f', 'admin@jquiver.com', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'), 
('4028b881765fd6570176609447520005', '/images', 'google-credentials-3.PNG', '67d1978e-1274-4e51-9a75-4ffbba12a8d3', 'admin@jquiver.com', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'), 
('4028b881765fd657017660d3c0550006', '/images', 'facebook-1.PNG', 'e0820928-668d-4558-bd8c-e4e9d190a764', 'admin@jquiver.com', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('4028b881765fd657017660d441bd0007', '/images', 'facebook-2.PNG', 'a9a61d99-4697-4482-b791-b651fa49373e', 'admin@jquiver.com', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'), 
('4028b881765fd657017660d70c6d0008', '/images', 'offcie365-1.PNG', '8f1ca6b2-b9e5-4dfa-ac9b-1976637abc4b', 'admin@jquiver.com', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'), 
('4028b881765fd657017660d8b7380009', '/images', 'offcie365-2.PNG', 'd1ef583f-6fc7-4a58-a5d8-9e015052f0f8', 'admin@jquiver.com', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'), 
('4028b881765fd657017660d92df9000a', '/images', 'offcie365-3.PNG', 'cad0118f-f44f-48fa-b6de-ff2b6fc74fa2', 'admin@jquiver.com', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'), 
('4028b881765fd657017660da0caa000b', '/images', 'offcie365-4.PNG', '4e4a9bd9-d0ed-44e0-9b14-263bcc39f25c', 'admin@jquiver.com', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'), 
('4028b881765fd657017660da9464000c', '/images', 'offcie365-5.PNG', 'bf29893b-b207-4982-b350-a32c8ca377a0', 'admin@jquiver.com', NOW() , 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),
('40289d8f76a600980176a60bfa870002', '/images', 'User_Details_In_JS.png', 'fe9a16c1-bf80-4269-92a3-83bc33b48864', 'admin@jquiver.com', NOW(), 'helpManual', 'e03447c8-eaa0-4119-b97e-b802bd8f4ff1'),

('40289d3d765124480176512a5e0f0000', '/images', 'Autocomplete_Example_1.PNG', 'cb9f36dd-18be-429a-b6be-9a6042f06c9a', 'admin@jquiver.com', NOW(), 'helpManual', '5e46df00-e07a-4b73-889f-2894adfd3df8'), 
('40289d3d765124480176512a6e660001', '/images', 'Autocomplete_Example_2.PNG', 'f77ad3ca-d27c-4a0a-a2e7-e9a4ebd776a0', 'admin@jquiver.com', NOW(), 'helpManual', '5e46df00-e07a-4b73-889f-2894adfd3df8'), 
('40289d3d765124480176512a79560002', '/images', 'Autocomplete_Example_3.PNG', '17fb0677-f999-4fe0-8cd9-7190098a7e8d', 'admin@jquiver.com', NOW(), 'helpManual', '5e46df00-e07a-4b73-889f-2894adfd3df8'),
('40289d3d765124480176512a89830003', '/images', 'Autocomplete_Example_4.PNG', 'bf5e5d91-56f2-4c75-89aa-458612aba3cd', 'admin@jquiver.com', NOW(), 'helpManual', '5e46df00-e07a-4b73-889f-2894adfd3df8'), 
('40289d3d765124480176512a96630004', '/images', 'MultiSelect_Example_1.PNG', '99a536d6-7dd4-4e7e-9702-6a312c14cc8a', 'admin@jquiver.com', NOW(), 'helpManual', '5e46df00-e07a-4b73-889f-2894adfd3df8'), 
('40289d3d765124480176512aa3f80005', '/images', 'MultiSelect_Example_2.PNG', 'db6394b8-d7a6-4231-be9a-f587794e2f99', 'admin@jquiver.com', NOW(), 'helpManual', '5e46df00-e07a-4b73-889f-2894adfd3df8'),


('40288089766a9a9501766acfa31b0002', '/images', 'export_config.PNG', '921e106e-a5d5-464e-a2f3-75902adeb49e', 'admin@jquiver.com', NOW(), 'helpManual', 'dd97c23d-feef-4cea-afcf-3cece7819159'),
('40288089766a9a9501766acfe7920003', '/images', 'preview_export.PNG', '8cb04e93-41c1-4d34-9e4a-5d0eb5b9c461', 'admin@jquiver.com', NOW(), 'helpManual', 'dd97c23d-feef-4cea-afcf-3cece7819159'),
('40288089766a9a9501766ad0ba300004', '/images', 'export_menu_db.PNG', '2278931c-ce43-4bb8-86dc-6b202b63b1a0', 'admin@jquiver.com', NOW(), 'helpManual', 'dd97c23d-feef-4cea-afcf-3cece7819159'),

('40288089766b4ac701766b5f263d0017', '/images', 'import_config.PNG', '12fb5a7f-ac0e-4dfc-b14d-a65310d75e1e', 'admin@jquiver.com', NOW(), 'helpManual', 'be37c240-2607-4d79-9ef1-136dbd7c524b'),
('40288089766b4ac701766b61fa4a0019', '/images', 'import_compare.PNG', '0945e9ce-b63d-4505-852d-491e56b4f74b', 'admin@jquiver.com', NOW(), 'helpManual', 'be37c240-2607-4d79-9ef1-136dbd7c524b'),

('40289d3d768650810176866b317e0003', '/images', 'File_Upload_Manager_Master.PNG', '3c60e10d-c0cd-4b60-944f-b9a54e9faa45', 'admin@jquiver.com', NOW(), 'helpManual', '6938f0ac-00fe-4b94-95e7-02ef72016fe4'), 
('40289d3d768650810176866b46ad0004', '/images', 'Add_File_Configuration_Step_1.PNG', 'dab82912-46c3-46e0-9d4e-d2b4abb1e283', 'admin@jquiver.com', NOW(), 'helpManual', '6938f0ac-00fe-4b94-95e7-02ef72016fe4'), 
('40289d3d768650810176866b52100005', '/images', 'Add_File_Configuration_Step_2.PNG', '750c9d79-1970-48da-8f8d-5ebcde096944', 'admin@jquiver.com', NOW(), 'helpManual', '6938f0ac-00fe-4b94-95e7-02ef72016fe4'),
('4028b88176a8f81a0176a9aad1090000', '/images', 'form-builder-welcome.png', '99300f8f-0ad6-4021-8bcd-f679a6f32b8c', 'admin@jquiver.com',NOW() , 'helpManual', '6938f0ac-00fe-4b94-95e7-02ef72016fe4'),
('4028b88176a8f81a0176a9ae0cf80001', '/images', 'form-builder-listing.PNG', 'eb941ec9-1eb2-47c4-a0cc-0ce53145a540', 'admin@jquiver.com',NOW(), 'helpManual', '6938f0ac-00fe-4b94-95e7-02ef72016fe4'),
('4028b88176ae0bd20176ae160c010000', '/images', 'form-builder-template.png', '2c5ac0a7-47fe-480b-b9c7-7982a2ec99a6', 'admin@jquiver.com', NOW(), 'helpManual', '6938f0ac-00fe-4b94-95e7-02ef72016fe4'), 
('4028b88176ae0bd20176ae1843480001', '/images', 'form-builder-populate.png', 'b6307aae-374f-499f-87f6-82add750c0e6', 'admin@jquiver.com', NOW(), 'helpManual', '6938f0ac-00fe-4b94-95e7-02ef72016fe4'), 
('4028b88176ae0bd20176ae1abd670002', '/images', 'form-builder-populate-select.png', '7a17a93c-4365-42c2-ab2f-b7a0e65254ac', 'admin@jquiver.com', NOW(), 'helpManual', '6938f0ac-00fe-4b94-95e7-02ef72016fe4'), 
('4028b88176ae0bd20176ae1bbc760003', '/images', 'form-builder-populate-save.png', '134dccf1-2974-48fc-aa51-021f096f6724', 'admin@jquiver.com', NOW(), 'helpManual', '6938f0ac-00fe-4b94-95e7-02ef72016fe4'),
('5d45f43e-5ba0-471a-8fa5-80de72d12eb0', '/images', 'mail_config.png', '5712df30-f66b-4569-a0ee-04e075fe1d41', 'admin@jquiver.com', NOW(), 'helpManual', '918676c8-b653-43ee-964a-d4faaeb13787'),

('0f684eec-041c-4e6e-8e69-025e487d3356', '/images', 'help_manual_1.png', 'f45512c5-4fe6-4741-abc6-933cb825630c', 'admin@jquiver.com', NOW(), 'helpManual', '4dddd5a0-d69f-4e6c-bd91-0fa10e8adbfb'),
('5831d7d7-d16c-4048-9bf1-d2ee1fdc127c', '/images', 'help_manual_2.png', '53407a0b-4672-43fc-be89-5e2503a9e2e9', 'admin@jquiver.com', NOW(), 'helpManual', '4dddd5a0-d69f-4e6c-bd91-0fa10e8adbfb'),
('b71e324a-4cd8-48e9-9740-fd792e9c7810', '/images', 'help_manual_3.png', '664d3641-2a28-461f-8146-3be79db10271', 'admin@jquiver.com', NOW(), 'helpManual', '4dddd5a0-d69f-4e6c-bd91-0fa10e8adbfb'),
('c8311426-74b8-4811-9319-37a04c47c598', '/images', 'help_manual_4.png', '655509c1-9244-4a43-8ed1-9213ad621630', 'admin@jquiver.com', NOW(), 'helpManual', '4dddd5a0-d69f-4e6c-bd91-0fa10e8adbfb');


REPLACE INTO jq_file_upload (file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id) VALUES
('1f178e13-be12-4c29-bc47-9910a867af6c', 'helpManual', '/images', 'Site_Layout_Add_Step_1.PNG', '433b8358-886f-4235-bfb2-4d11c1c58256', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),  
('dc8c094d-700b-40a9-8d7c-49e1aa55e1dd', 'helpManual', '/images', 'Site_Layout_Add_Step_2.PNG', '7d57f226-dfc9-4339-8837-c5236e351f9b', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('00b01a2d-bdeb-40aa-b457-169988480295', 'helpManual', '/images', 'Site_Layout_Add_Step_3.PNG', '2475102b-eee9-43a7-b4b9-463bbe829531', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e');


REPLACE INTO jq_file_upload (file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id) VALUES
('09f4260c-0cef-4569-b806-7589ee2a108f', 'helpManual', '/images', 'Dashboard_Inside_Menu_Step_1.png', 'b6580410-a2b2-4831-9315-e8165298fd08', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('6011972c-0544-4322-8bb8-6229463682cb', 'helpManual', '/images', 'Dashboard_Inside_Menu_Step_2.png', '251cfb51-99f8-432d-8cfa-cc069f498636', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('c288ea4f-e848-425a-9e1a-c0284d447079', 'helpManual', '/images', 'Dashboard_Inside_Menu_Step_3.png', '6a8a351a-4f86-4a81-896e-728b84aa800f', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('5d4065f3-78d7-4480-be77-ac3f35dff0ff', 'helpManual', '/images', 'Dashboard_Inside_Menu_Step_4.png', 'bc2ba223-cce8-43e0-8cbf-af53e9b45752', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('b0c436b2-4c76-49f7-b960-a537c09e7d21', 'helpManual', '/images', 'Dashboard_Inside_Menu_Step_5.png', '2ab7a19c-5c34-4b02-af57-daa168ba156f', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e');
 
 
REPLACE INTO jq_file_upload(file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id) VALUES
('6f796eb0-bafa-4dcd-9c8a-d2469be048fe', 'helpManual', '/images', 'Dynamic_Form_Inside_Menu_Step_4.png', 'f09580dc-6235-4cbf-ad3f-12f1d405ed29', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('9f441835-28b8-425d-b21f-abbced54dfa2', 'helpManual', '/images', 'Dynamic_Form_Inside_Menu_Step_2.png', '8146c655-4805-4812-9f33-7690216996f2', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('c4fd6ce2-5071-4962-bc8f-56bfce3b2907', 'helpManual', '/images', 'Dynamic_Form_Inside_Menu_Step_1.png', 'fd6a3e46-8c0e-447a-9011-8426d801158a', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('da04a076-ed62-4f42-b1e8-91372a3e0fc4', 'helpManual', '/images', 'Dynamic_Form_Inside_Menu_Step_3.png', 'ed0ee55f-01e6-4c7d-b57d-1363e9020a4f', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('f24d2e80-8b38-48a0-bfac-5a2488736086', 'helpManual', '/images', 'Dynamic_Form_Inside_Menu_Step_5.png', '13a170c6-9fa1-482e-80a0-a8bec500a084', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e');


REPLACE INTO jq_file_upload(file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id) VALUES
('3860998e-33ab-4dcb-a27d-0cce0aa73337', 'helpManual', '/images', 'REST_API_Inside_Menu_Step_5.PNG', '8f96d948-c4cd-4038-a5a3-b8861d4d932c', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('59ad8aa2-fcbe-4ff8-85b4-b92fb09d2b81', 'helpManual', '/images', 'REST_API_Inside_Menu_Step_2.PNG', '41b34f24-3523-48a7-bace-d814aff8f60b', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('717c32c4-be30-47d7-b55e-25e11b20b195', 'helpManual', '/images', 'REST_API_Inside_Menu_Step_1.png', 'd5756ba3-a3b7-4745-875c-feb60e0c8690', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('a3f2c013-dd80-4cd0-957f-a3ae8da725e1', 'helpManual', '/images', 'REST_API_Inside_Menu_Step_4.PNG', '942e34bb-763c-4425-8b53-65ae682616ca', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('fc522253-f73a-4980-a43b-c096d82e6081', 'helpManual', '/images', 'REST_API_Inside_Menu_Step_3.PNG', '3994b593-c055-4f8c-9a2e-5620a0ca71bb', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'); 


REPLACE INTO jq_file_upload(file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id) VALUES
('2942ed27-f194-459a-8121-0106b9d3d9a4', 'helpManual', '/images', 'Template_Inside_Menu_Step_1.png', '63027d6b-0c5a-4d0a-ba06-389028759f6d', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('72d8474a-7d4d-4aa8-8528-833679baf9be', 'helpManual', '/images', 'Template_Inside_Menu_Step_2.png', '40cdc11c-2c5f-4852-aa2b-92f9100176e1', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('a3f4f471-0386-40ee-b21b-e91841083822', 'helpManual', '/images', 'Template_Inside_Menu_Step_3.png', '025fdd3a-2404-4cfb-a5c0-c39a000ba0d4', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('649fab90-76a9-4135-b271-71b9388140a7', 'helpManual', '/images', 'Template_Inside_Menu_Step_4.png', '33190738-3180-4c41-859c-cf01597d25a2', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('a3e471fd-f9be-4987-bb4f-9db93a9cbb06', 'helpManual', '/images', 'Template_Inside_Menu_Step_5.png', 'b5e0a006-0309-4b59-a73a-6df82b71a734', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e');


REPLACE INTO jq_file_upload(file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id) VALUES
('577dfda4-41cb-4c8a-ad9d-bdb769140d36', 'helpManual', '/images', 'Role_Based_Home_Page_Step_1.png', 'fc8495bc-1184-4e5b-ad32-949ec461f24a', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('44c3eaa9-82a7-4c66-8ba5-af20ab5ff087', 'helpManual', '/images', 'Role_Based_Home_Page_Step_10.png', '24dab333-fc53-40f5-8983-6930858a0a5b', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('ecb167a8-bb55-4c23-be00-32b88e8903d1', 'helpManual', '/images', 'Role_Based_Home_Page_Step_11.png', 'ad604204-33ff-4d55-930e-7a9b8211d8d4', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('56ebcae1-14b7-4855-b5d5-9e936c961347', 'helpManual', '/images', 'Role_Based_Home_Page_Step_12.png', '03690127-cea1-4e3c-8202-b865447e8f00', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('f207f7fb-0a09-471d-b17d-93f1b55f0f86', 'helpManual', '/images', 'Role_Based_Home_Page_Step_2.png', 'aca811d4-d0e9-4124-aa87-567c8608467a', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('ac0a0f40-cbc8-4f52-8125-a2d7808a2ff4', 'helpManual', '/images', 'Role_Based_Home_Page_Step_3.png', 'f89270d9-2c2d-4ecc-82bd-467ab2fa952c', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('0a7a4f60-8819-43fd-aa85-90a02bb042ba', 'helpManual', '/images', 'Role_Based_Home_Page_Step_4.png', '2cc37aa0-e941-48ab-a174-7967cb95ce82', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('4daeff56-7cbc-4f9b-ba29-3c60021ec634', 'helpManual', '/images', 'Role_Based_Home_Page_Step_5.png', 'b329c929-edb7-41e9-905a-49c0e409df14', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('29f15fe9-bd19-4f4a-a4c9-ec198a9b078b', 'helpManual', '/images', 'Role_Based_Home_Page_Step_6.png', '29f61697-ac85-49f6-90c7-3b4aeb3595b9', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('9656a918-91c6-4352-8dbb-07860e404e53', 'helpManual', '/images', 'Role_Based_Home_Page_Step_7.png', '376a790c-076e-49b5-a912-d0ce63239e15', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('ecbb9c05-228b-4a0d-b65c-202891905318', 'helpManual', '/images', 'Role_Based_Home_Page_Step_8.png', 'a41d3d1a-ff19-45b0-ac86-3f3a6dbc5870', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e');

DELETE FROM jq_file_upload WHERE file_upload_id = 'fc7c2308-b17f-41b8-befc-64a383f45572';

REPLACE INTO jq_file_upload(file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id) VALUES
('93d0e859-4726-41ea-af6d-743258cbecd1', 'helpManual', '/images', 'Site_Layout_Group_Step_1.PNG', '82d226ad-56b6-4d48-9cbc-c08c393bff10', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'),
('758ba2cd-ff8a-4865-94b2-4d062ffab93c', 'helpManual', '/images', 'Site_Layout_Group_Step_2.PNG', '625cbc3a-164e-41f3-a4e8-e7bcc8709712', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('553b2332-bf8b-462b-9c5c-d89bc0682a92', 'helpManual', '/images', 'Site_Layout_Group_Step_3.PNG', '25b51eba-91d4-4ac7-bbd7-2de0e4f6881f', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('b3fd7730-a501-4a50-afed-54a956151748', 'helpManual', '/images', 'Site_Layout_Group_Step_4.PNG', '4a7c9f70-5626-4e7d-8098-f6b5f6a18113', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e');


REPLACE INTO jq_file_upload(file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id) VALUES
('b92c317e-d2fd-44b5-a972-9ca18af4705d', 'helpManual', '/images', 'Site_Layout_Path_Variable_Step_1.PNG', '7b74f2b0-4a42-40a2-9730-b6404055e698', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('8aa4e0ca-f775-404e-972f-396bf639390e', 'helpManual', '/images', 'Site_Layout_Path_Variable_Step_2.PNG', '37e52e84-f59e-44c1-9dfc-e9ef80e7dfd6', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e'), 
('c41a3346-5a70-4c56-b015-76f527c389cd', 'helpManual', '/images', 'Site_Layout_Path_Variable_Step_3.PNG', 'd809736c-085f-4188-9312-6608a47f8bd7', 'admin@jquiver.com', NOW(), '68613ad7-8596-4c48-94e6-f752ab53eb4e');


REPLACE INTO jq_file_upload(file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id) VALUES
('5ea356bb-88d3-4097-b44d-d96ec273ed2f', 'helpManual', '/images', 'Master_Generator_1.png', '3fad5943-291d-4677-bff7-a3d1972fadd3', 'admin@jquiver.com', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'), 
('6cdc83c4-e2b2-4eb8-b9cb-17aff7c51a58', 'helpManual', '/images', 'Master_Generator_2.png', '398a7815-1709-48fc-a19c-c1c44072aa6f', 'admin@jquiver.com', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'), 
('8b512d2c-6f59-433b-a6b4-b1bdd22479b3', 'helpManual', '/images', 'Master_Generator_3.png', 'd18dbccb-eddc-4407-ac07-ab707d632ec7', 'admin@jquiver.com', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'), 
('8d1522cb-a12a-474e-a870-2d63098b620b', 'helpManual', '/images', 'Master_Generator_4.png', 'f5b9927b-7f12-426f-ba65-9932a1a0b7b3', 'admin@jquiver.com', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'), 
('97df2f91-924d-4d86-b999-72f43bf4c2bc', 'helpManual', '/images', 'Master_Generator_5.png', '6a9a57d9-9fde-4077-a59b-8a4c5c2cfb07', 'admin@jquiver.com', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('9e9fcdb7-f54d-4c04-aee5-76a35a0559d8', 'helpManual', '/images', 'Master_Generator_6.png', 'e4a6b905-5d1d-41a3-b409-a1b5c5f8d967', 'admin@jquiver.com', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('d560cd3c-82f8-4c7b-927b-77c679cdcf51', 'helpManual', '/images', 'Master_Generator_7.png', 'b995d553-986f-460b-b204-b25614a71be5', 'admin@jquiver.com', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('c3779826-2f8b-42d1-afeb-8302482f468f', 'helpManual', '/images', 'Master_Generator_8.png', 'dfe0bb67-d499-45e8-b0e2-6899ddca5d27', 'admin@jquiver.com', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('66b7db46-3bbf-40fc-b0aa-24ae8e3febba', 'helpManual', '/images', 'Master_Generator_9.png', '59d7833b-1980-4917-a30c-f3211c8d7a3c', 'admin@jquiver.com', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'), 
('68d76985-5616-4fb4-a481-b25fd6ccf888', 'helpManual', '/images', 'Master_Generator_10.png', '5de7a2a8-5cfb-45c8-8cc6-f1776ffbddfe', 'admin@jquiver.com', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'), 
('88260a66-eaaf-40fb-b383-518e131cb6f9', 'helpManual', '/images', 'Master_Generator_11.png', '8ddbe404-ea02-46d3-8e88-034819ff6990', 'admin@jquiver.com', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'), 
('a3f74af1-8159-4134-942f-5d4655d4203e', 'helpManual', '/images', 'Master_Generator_12.png', 'b76177d8-1476-4105-817d-45fdf1eb32e6', 'admin@jquiver.com', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'), 
('a501ef3d-bcc9-4d42-8990-99ddeb97fbe4', 'helpManual', '/images', 'Master_Generator_13.png', '2696c9d5-f829-425b-a6ea-af5b56f22e88', 'admin@jquiver.com', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('a6b9c378-6ffc-4874-870e-271d829103cf', 'helpManual', '/images', 'Master_Generator_14.png', '6a1c51fc-1c1c-4c94-853e-ffd9633bc1b3', 'admin@jquiver.com', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('b0313ee9-32b5-42bc-8b8c-9ad79c942c42', 'helpManual', '/images', 'Master_Generator_15.png', '2ed613e3-5e90-4071-954f-36b726219da9', 'admin@jquiver.com', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('e8534c20-a6cf-456a-82bc-8290c4a14792', 'helpManual', '/images', 'Master_Generator_16.png', 'c73f141b-f96c-430c-8429-a70cdd57d7c6', 'admin@jquiver.com', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46'),
('f625d2c2-6073-46a3-adc0-64d903aeaaa7', 'helpManual', '/images', 'Master_Generator_17.png', '2ae0daec-3ade-455f-919e-2ae7ef44cf5c', 'admin@jquiver.com', NOW(), '61fb4be9-a197-4759-9a4c-b885ce973f46');



REPLACE INTO jq_file_upload(file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id) VALUES
('1bd91c6b-3f30-4481-8803-f9a8fe8b8626', 'helpManual', '/images', 'Project_Name_Change_Password.PNG', '91bea704-9087-43d9-94d2-8bffce12940a', 'admin@jquiver.com', NOW(), '17feffba-99f4-4591-9cb5-0fef46ee0b77'), 
('4ab78613-7f7b-46b5-af26-73a3643aaea0', 'helpManual', '/images', 'Project_Name_Home_Page.PNG', 'f51ffbc2-6de8-4fe7-91ef-616834458d33', 'admin@jquiver.com', NOW(), '17feffba-99f4-4591-9cb5-0fef46ee0b77'), 
('8b01d2d4-1bd8-4a54-a460-f50202cd0471', 'helpManual', '/images', 'Project_Name_Login_Page.PNG', '1815a60e-4ad0-4f96-9e82-8e638c692c16', 'admin@jquiver.com', NOW(), '17feffba-99f4-4591-9cb5-0fef46ee0b77'), 
('8261a46a-3ece-4ff6-a81e-742ac55d1cfa', 'helpManual', '/images', 'Project_Name_Reset_Password.PNG', '82360982-7fca-4ebe-9ada-8f5b12dfce14', 'admin@jquiver.com', NOW(), '17feffba-99f4-4591-9cb5-0fef46ee0b77'), 
('72ad938d-a6de-4d16-9203-27cc8b12f649', 'helpManual', '/images', 'Project_Name_Reset_Password_TOTP.PNG', '3ca18b25-e32e-40ff-bea9-667bd4ec8b39', 'admin@jquiver.com', NOW(), '17feffba-99f4-4591-9cb5-0fef46ee0b77'), 
('1fca4824-251f-4426-abdc-8cff655fbfa4', 'helpManual', '/images', 'Project_Name_Resource_Key_1.PNG', 'ce6e2730-3d8a-4684-869c-55907fc16601', 'admin@jquiver.com', NOW(), '17feffba-99f4-4591-9cb5-0fef46ee0b77'), 
('3a1e89b9-f0cb-42aa-ab7d-041e08daf536', 'helpManual', '/images', 'Project_Name_Resource_Key_2.PNG', '173c0ba8-52c4-43e6-90cd-b55f71bdf8bb', 'admin@jquiver.com', NOW(), '17feffba-99f4-4591-9cb5-0fef46ee0b77');

REPLACE INTO jq_file_upload(file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id) VALUES
('03f110de-811d-47f3-be2e-fc6196c11ace', 'helpManual', '/images', 'File_Bin_1.PNG', '13ff9b2e-1410-4346-b893-8d82f4e39e69', 'admin@jquiver.com', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'), 
('05777b2b-c424-4472-8678-363d4fe3ac5d', 'helpManual', '/images', 'File_Bin_2.PNG', 'c020f88f-af4f-4b1e-a46b-63663722cd97', 'admin@jquiver.com', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'), 
('62765d11-c5ad-4128-a94a-f55ac5b36bbe', 'helpManual', '/images', 'File_Bin_3.PNG', 'f922428f-9fee-482d-857b-84804ca99e28', 'admin@jquiver.com', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'), 
('6908081e-3441-40f0-87db-3e55f00feb83', 'helpManual', '/images', 'File_Bin_4.PNG', '36d1314c-1545-4010-a1af-4f5ad77470d9', 'admin@jquiver.com', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'), 
('6d2b0993-be4c-43c8-a72f-19eaf5a6ee93', 'helpManual', '/images', 'File_Bin_5.PNG', '76e5284c-5b03-4aa3-8775-a954067455dd', 'admin@jquiver.com', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'), 
('74909e25-3efa-41c7-a11f-8e02dcc1768c', 'helpManual', '/images', 'File_Bin_6.PNG', '0f1f2aef-7a8f-4c53-a214-6b4c7e1412c0', 'admin@jquiver.com', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'), 
('8606b360-59e0-469f-86ea-737605981c04', 'helpManual', '/images', 'File_Bin_7.PNG', '8f54cc6c-bc40-4915-90d3-d5cc4bb8c61d', 'admin@jquiver.com', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'),
('b71984b5-841b-4b6e-bffa-77ca956105dc', 'helpManual', '/images', 'File_Bin_8.PNG', '8a7082db-8725-4b52-bb18-72e9695c9c07', 'admin@jquiver.com', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'), 
('c57e84f0-aa52-4597-98d8-9eb2afd90cc5', 'helpManual', '/images', 'File_Bin_9.PNG', '84c266c3-57c7-4974-82dd-06e982c7d9ff', 'admin@jquiver.com', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'), 
('8c901fa8-4c71-48dd-bd1c-f2e6bdba2fce', 'helpManual', '/images', 'File_Bin_11.PNG', '677b6e6f-83fc-4fbb-8641-ad0f822fad98', 'admin@jquiver.com', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'), 
('a318aa76-ce66-446e-8418-fdbc62ede7c8', 'helpManual', '/images', 'File_Bin_12.PNG', '9b959e22-2ac8-4321-ab90-f1fa4d7a6de5', 'admin@jquiver.com', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538'), 
('f288f186-d985-4a53-abb1-4b293ef92bf1', 'helpManual', '/images', 'File_Bin_13.PNG', '03daf48e-e9b8-4440-9e74-2610ee6bd628', 'admin@jquiver.com', NOW(), '8b425d39-72f0-4c6f-b71c-4ef247979538');


REPLACE INTO jq_file_upload(file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id) VALUES
('6079c4c4-167a-4a16-bdee-56c9f93f986b', 'helpManual', '/images', 'REST_API_FTL_Example.PNG', '4d214e32-4b3f-4621-b454-b5ce61878cce', 'admin@jquiver.com', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'), 
('c6b5c730-804f-4aab-9a22-ea8d2b32ad63', 'helpManual', '/images', 'REST_API_FTL_STEP_1.PNG', 'dbf619bb-353f-4745-a695-ffcf988bbff4', 'admin@jquiver.com', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'), 
('29792cef-d7e7-43ba-a15a-36ec20e6949d', 'helpManual', '/images', 'REST_API_FTL_STEP_2.PNG', '6c288ac4-dc60-44ae-a787-276397a4f50c', 'admin@jquiver.com', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'), 
('3ea2a9eb-81bf-4e89-b904-a29d7e6eb709', 'helpManual', '/images', 'REST_API_FTL_STEP_3.PNG', '951c7d6c-a9b1-48c5-bbd4-baa224c6c8d2', 'admin@jquiver.com', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'), 
('b3e3c0c6-62f4-41f0-9931-3ab2a378e5ee', 'helpManual', '/images', 'REST_API_FTL_STEP_4.PNG', '7ab2027e-494b-4ef7-9a5f-eb3f3af8a6f0', 'admin@jquiver.com', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9');


REPLACE INTO jq_file_upload(file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id) VALUES
('84dfcea6-4f20-44c6-b33e-c76cedbf3053', 'helpManual', '/images', 'REST_API_JAVA_Example.PNG', 'db97403b-02d6-4236-be08-fc0c03676051', 'admin@jquiver.com', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'), 
('f63b06f3-ae5d-48f0-9d9a-0556fa98c8f4', 'helpManual', '/images', 'REST_API_JAVA_STEP_1.PNG', '3ec982f5-9e63-4bc1-9688-ceef23f50147', 'admin@jquiver.com', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'), 
('92f1b314-0c50-4c4b-aaca-39c18f003eed', 'helpManual', '/images', 'REST_API_JAVA_STEP_2.PNG', '3abf27bc-20be-4818-977d-90e339ffc9e4', 'admin@jquiver.com', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'), 
('8f2e0a09-0088-4adf-a224-4f4a50a9a894', 'helpManual', '/images', 'REST_API_JAVA_STEP_3.PNG', '55a87e57-27e2-4e45-9c28-cbd59a6424ab', 'admin@jquiver.com', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'), 
('8984531a-367d-4f25-b3a8-80c7cb9c8066', 'helpManual', '/images', 'REST_API_JAVA_STEP_4.PNG', '14d17671-a791-4989-a9dc-98a9e7377888', 'admin@jquiver.com', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'), 
('512a2273-df0e-40b2-b482-2927aee32c2c', 'helpManual', '/images', 'REST_API_JAVA_STEP_5.PNG', 'ae5e0984-c13b-4e64-9529-6a4857a9ba3a', 'admin@jquiver.com', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'), 
('89ad8745-d78a-4abf-8793-7e7001a288ca', 'helpManual', '/images', 'REST_API_JAVA_STEP_6.PNG', '50e9f2b8-4525-4c6b-bd57-71dd8c7b3653', 'admin@jquiver.com', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'), 
('78827b0f-e616-4352-b70d-11588f712a83', 'helpManual', '/images', 'REST_API_JAVA_STEP_7.PNG', '786d48be-5b7c-4fe8-ad08-1019e3501393', 'admin@jquiver.com', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9');


REPLACE INTO jq_file_upload(file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id) VALUES
('ddefefe2-b9ad-4cac-871f-28cefb134208', 'helpManual', '/images', 'REST_API_JS_STEP_1.PNG', '35e3530d-445f-44d5-a6bc-cccc90719b48', 'admin@jquiver.com', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'), 
('c5ce38d5-b8f6-4ec1-a947-b0db7963812c', 'helpManual', '/images', 'REST_API_JS_STEP_2.PNG', 'ad8d44ef-5d4c-4c50-a0f6-8e6f962b3dde', 'admin@jquiver.com', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'), 
('377f1c16-03a2-4202-81d1-8151be81fb1a', 'helpManual', '/images', 'REST_API_JS_STEP_3.PNG', '9c527c6f-ab95-42e9-9606-594b0ed5aaf3', 'admin@jquiver.com', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'), 
('3009a9cb-dcae-49ba-a3c2-c13ce62750ef', 'helpManual', '/images', 'REST_API_JS_STEP_4.PNG', '9e9452ba-6b47-4aa1-be82-85eee9a93718', 'admin@jquiver.com', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9'), 
('2e5939e8-8d84-42b5-9f92-3a9c590e5a98', 'helpManual', '/images', 'REST_API_JS_STEP_5.PNG', 'e723fb52-7fdf-4e2a-9e58-a190d2d033d4', 'admin@jquiver.com', NOW(), '81c506ff-dab5-43de-a790-58af356de3e9');


REPLACE INTO jq_file_upload(file_upload_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_bin_id, file_association_id) VALUES
('7bba0ef5-1416-4d3c-84bc-a48e2d0777ec', '/images', 'validation_message.png', 'ebdc1320-7a4d-440b-9bd4-f0324b0d2894', 'admin@jquiver.com', NOW(), 'helpManual', '50f92287-c438-4ee2-bdfa-b4e710d2e64b'),
('11363af4-6efe-4e18-95dc-8ed6c83033d4', '/images', 'others_1.png', '0abb596e-6ac4-4634-b25f-9d74cc460848', 'admin@jquiver.com', NOW(), 'helpManual', '50f92287-c438-4ee2-bdfa-b4e710d2e64b'),
('881f45cd-2e96-4c62-8b0e-65bc2925dd0b', '/images', 'others_2.png', '4f85084d-419f-4584-88a6-31e190525cac', 'admin@jquiver.com', NOW(), 'helpManual', '50f92287-c438-4ee2-bdfa-b4e710d2e64b'),
('9674702e-c1e5-4a67-8f98-f167871a2207', '/images', 'others_3.png', '75ce0e2f-2020-4831-85ac-f0f1a42afb98', 'admin@jquiver.com', NOW(), 'helpManual', '50f92287-c438-4ee2-bdfa-b4e710d2e64b');

SET FOREIGN_KEY_CHECKS=1;


