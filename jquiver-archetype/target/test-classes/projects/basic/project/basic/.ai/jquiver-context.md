# JQuiver Project Context

JQuiver is a Java/Spring Boot based low-code application platform.

It supports database-driven configuration, metadata-driven routing, Dynamic REST APIs, Form Builder, Form.io integration, Grid Utils, dashboards, dashlets, multilingual resource bundles, templates, file upload bins, role-based access, scheduler, notifications, API clients, script libraries, business modules, tags, and reusable application modules.

This project was generated from the JQuiver Maven archetype.

## Typical JQuiver components

- Java/Spring Boot backend.
- MariaDB or compatible relational database.
- jQuery-based UI components.
- pqGrid-style listing components.
- Module router and menu metadata.
- Form Builder.
- Form.io pluggable forms and embedded external forms.
- Dynamic REST API engine.
- Additional datasource metadata.
- Autocomplete/typeahead metadata.
- File upload bins and custom file bins.
- Template engine, email/XML, and webclient XML templates.
- Resource bundles and multilingual labels.
- Dashboards and dashlets.
- Scheduler jobs and logs.
- Notifications and mail flows.
- API clients.
- Script libraries.
- Business modules and tags.
- User, role, authentication, and permission metadata.

## Known instance patterns from KB examples

- Ark-style instances can include a main JQuiver schema, a custom schema, additional datasource usage, and Form.io embed examples.
- SBI FAC-style instances can include public job/application flows, applicant upload bins, interview management, dashboards, and schedulers.
- HRS-style instances can include large HRMS metadata, audit management, interview management, L&D, consultant/separation workflows, scheduler/mail patterns, API clients, script libraries, business modules/tags, and activity-log integration through Dynamic REST service logic.

## Important

Do not assume this is a plain Spring Boot project.

Always prefer JQuiver platform conventions and verified metadata/source behavior.
