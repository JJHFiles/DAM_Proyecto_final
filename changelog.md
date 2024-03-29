# CHANGELOG for blist proyect



## 0.2.0 2021-11-29
### Added
- Delete group option on edit group 
- Editor users can exit from groups

### Changed
- Removed all characters limits of inputs. Now admit symbols with security
- Visual edits on add group and group edit screens
- FAB size values
- Filter button size values
- Profile icons and size values
- Invoice item list layout
- Removed nigth mode files
- Navigation transactions
- Text on registry flow in final button
- Registry and change password navigations
- Disable buttons with unfull forms
- Button disabled styles

### Fixed
- Roles check
- Strings.XML from classes and XMLs checked
- Hardcoded texts
- TODO checked and removed

### Security
- SQL Inyection fixed for all backend scripts
- Use email/password validation to all Volley request
- WbApiRequest isInvoiceByGroup changed to validate user/password

### Removed
- Depuration toast




## 0.1.8 2021-11-27
### Added
- MyData screen added

### Changed
- New empty illustration
- Navigation on home screen
- Profile pic
- Now editor user cannot delete/add invoices and edit groups
- Add group button only activated on full form 

### Fixed
- Constraint of empty invoice screen
- Bad name showed on profile screen
- Bad width values on registry screen


### Security
- Add Group screen not admit especial characters on name, description and email inputs
- Add invoice screen not admit especial characters on multiple inputs
- edit group screen not admit especial characters on multiple inputs
- SQL Inyection for many backend scripts


### Removed
- Unused and unnecesarry code
- Calendar utility (comment)



## 0.1.7 2021-11-23
### Added
- New screen with invoice detail
- Now can delete invoice on invoice detal
- Now can upload PDF with new invoice
- New icons
- New button style for delete options
- Now can upload invoices with PDF from camera
- DB files on db root folder

### Changed
- Navigation logic to better UX
- FAB style

### Fixed
- Now can upload a invoice without PDF file
- Hardcoded text
- Icons with background
- Bad Size of views
- Edit invoice and Edit group by permission

### Removed
- Comments and unnecesary code 
- TODOs
- ScanLibrary module





## 0.1.6 2021-11-16  
### Added
- Navigation fixed to best experiencie and forbidden back on some screen
- Invoice name can be duplicated in multiple groups
- Now Add invoice screen show calendar text on local format 
- Now can't add invoices if all form inputs are complete
- Now tab fragment navigation not add to backstack for better nav
- Change password is available
- Spash screen animation to startup

### Changed
- Now some fragment use newInstance method instead constructor and bundle
- Now some fragment/activity receive group object instead parameters
- Some Toast was removed instead log depuration
- Icon for activitys
- Package refactored
- Calendar methods deprecated
- EditGroup with full features and full depurated

### Fixed
- InvoiceModel not load/write provider
- Warning minor
- Format date on activity


### Removed
- Some Toast was removed for better ux
- Unncesary code




## 0.1.5 2021-11-12
### Added
- Walktrought flow screens
- MainActivity for start flow (to walktrought, to login or to home)
- Added menu with "edit" option to empty groupinvoices screen

### Fixed
- Repair minor warning
- Clean code

### Removed
- Unused imports and variables




## 0.1.4 2021-11-12
### Changed
- Activitys list changed string adapter to custom for a styled design




## 0.1.3 2021-10-31
### Added
- New function: now can show multiples chart with invoices information
- New function: now can filter invoices to show in list and charts




## 0.1.2
### Added
- Group detail screen to explorer invoice
- Can add invoice to group
- Can swith to list invoice and chart invoice panel




## 0.1.1
### Added
- Login Screen with username/password and google access
- Registry flow for new users
- Home Screen with 3 functions
  - BList Screen to manage BList groups
  - Activity Screen to show actions historicals
  - Profile Screen to user options
- Create blist groups function
- Logout option on profile screen




## 0.1.0
### Added
- Project base with gitignore
