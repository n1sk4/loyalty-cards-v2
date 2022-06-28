# DIPLOMSKI RAD

Loyalty cards
====================================
About
-----
This app was developed as a starting point of mine Master thesis at Polytechnic of Zagreb.
The app that stores loyalty card data came as an idea when my wife told me that she has too many
loyalty cards to fit in her wallet or even bag...

Loyalty cards application, allows users to add, remove and update Loyalty cards.
Having that in mind, you can add/change:
 * Store name
 * Barcode number
 * Store logo

Another feature is using Google Vision to scan the barcodes for the ease of entering Barcodes.
The app allows scanning of 1D or 2D barcodes, and the user can select the type based on preference,
or you can enter the barcode by yourself.

App's logo can also be set as the user wishes, selected from gallery or by taking a picture.

Data storage
--------------
This app uses SQLite database, in which the Stores are saved. 
The database is stored in local data repository of the device, so the app is autonomous of the Internet.
Database is consisted of four columns:
 * Id - integer
 * Store name - string
 * Store barcode - string
 * Store logo - blob (Bitmap)

App accesses the data by the uid and then uses the name as-is,
barcode is generated in the runtime,
while the logo is generated from BLOB to Bitmap in the runtime.

Home screen
-----------
Opening the app the first time will direct you to the MainActivity which contains:
 * Recycler View
 * Floating Action Button
 * Menu


<p align="center">
<img src="https://user-images.githubusercontent.com/92214769/173567911-34e89278-f9df-4718-b088-f9cb79c71f4e.jpg" alt="drawing" width="150"/> __  <img src="https://user-images.githubusercontent.com/92214769/173567969-eccdbc02-bddd-47ab-9207-af26871654f3.jpg" alt="drawing" width="150"/>
</p>
  
<p align="center">
<img src="https://user-images.githubusercontent.com/92214769/173572687-0da79dc2-64b5-4504-a1ac-bc48fcca513a.jpg" alt="drawing" width="150"/> __ <img src="https://user-images.githubusercontent.com/92214769/173572726-26410848-9744-4260-9f35-54fbe64c347d.jpg" alt="drawing" width="150"/>
</p>

Recycler view is populated in the runtime, with the data from the SQLite databse.
Each item from the recyler view is a custom card layout that contains the store name and logo.
If the database is empty, home screen shows an icon and text displaying that You don't have any
Loyalty cards stored in the database.
The menu contains only one option: delete all data, which will empty out the database and reset the uid index.

When a few stores had been added, You can click and drag the stores to change the order in which they are placed,
so you can have more frequently used cards on top of the list.

Add new
=======

<p align="center">
<img src="https://user-images.githubusercontent.com/92214769/173568025-fcb72178-245d-420d-ac20-cad0c73c456d.jpg" alt="drawing" width="150"/> => <img src="https://user-images.githubusercontent.com/92214769/173568105-2f79fe2a-8201-4e5c-b701-c0bc77a6be46.jpg" alt="drawing" width="150"/> => <img src="https://user-images.githubusercontent.com/92214769/173568186-25b15cc9-6afc-4373-88ad-5cb14741d753.jpg" alt="drawing" width="150"/> 
</p>


Add name
--------
Adding a new store is done by pressing on the add Floating Action Button, which starts the Activity for adding a new store.
First thing You need to input is the store name, that is required to create new store. When You input the name,
Button *NEXT* will apear, which will lead you to add barcode Activity.

<p align="center">
<img src="https://user-images.githubusercontent.com/92214769/173568025-fcb72178-245d-420d-ac20-cad0c73c456d.jpg" alt="drawing" width="150"/>  <img src="https://user-images.githubusercontent.com/92214769/173568033-658b5415-e890-4e33-9813-216635b398be.jpg" alt="drawing" width="150"/>
</p>
  
Add barcode
----------
After adding a name, you'll be able to enter a barcode, either manually or by clicking on the *Capture image* button.
If you decided to capture image, system will ask You which data picker you want to use, then select the image that contains a barcode.

*Google Vision should be able to find the barcode even if you dont crop, so don't bother :)*

After the *Enter barcode* text input field has been filled, You can generate a barcode to see how it'll look like.
With the switch underneath, You can switch between 1D and 2D(QR) barcodes and display them in the ImageView field.
When you're done with editing the barcode, You can click *Next* button to start add logo Activity, or click
*Return* button to return to add name Activity.

*If the barcode field is empty, you will be promted a message that the barcode is missing and you can ignore the messafe or return to input the data*

<p align="center">
<img src="https://user-images.githubusercontent.com/92214769/173568105-2f79fe2a-8201-4e5c-b701-c0bc77a6be46.jpg" alt="drawing" width="150"/>  <img src="https://user-images.githubusercontent.com/92214769/173568128-6a8f7597-fe58-406f-afc0-1e44250b226c.jpg" alt="drawing" width="150"/>
</p>

<p align="center">
<img src="https://user-images.githubusercontent.com/92214769/173568138-81b584e9-a862-471b-9709-e795bb9321d3.jpg" alt="drawing" width="150"/>  <img src="https://user-images.githubusercontent.com/92214769/173568153-683871c8-ca11-4332-82c5-0d59fe94b33e.jpg" alt="drawing" width="150"/>
</p>
  
Add logo
--------
Adding a logo to your store is not necessary, but it'll improve the UX. To add a store logo, You can press on *Capture image* button or click on the 
placeholder icon to open data picker of your choice.

*The image can then be cropped or rotated to fit your demands*

After finishing the Activity, You can either select to return to edit barcode or name data, or click *Next* button to finish the adding Activities.

<p align="center">
<img src="https://user-images.githubusercontent.com/92214769/173568186-25b15cc9-6afc-4373-88ad-5cb14741d753.jpg" alt="drawing" width="150"/> <img src="https://user-images.githubusercontent.com/92214769/173568195-80da45f7-1edc-4995-801b-f2d18e6d0bf8.jpg" alt="drawing" width="150"/>
</p>
  
Showing barcodes
================
The main feature of this app is to show the barcode to the store cashier. 
You can access the store's barcode by tapping on the store item in the home screen, which will redirect You
to the show barcode Activity. There, You'll be able to see youre store's barcode and select the option to edit the store's data.

<p align="center">
<img src="https://user-images.githubusercontent.com/92214769/173571403-0acc9da1-39c2-46cf-a206-61f39002c7cf.jpg" alt="drawing" width="150"/>
</p>

Updating store's data
=====================
Updating the data can be accessed by clicking on the store from the home Activity, and then clicking on the *Edit data* button.
This will in turn, start the update Activity. There You have the option to change the name, barcode or the logo of the seleted store.
After You're done with editing, just click the *Update* button, and the Activity will finish and redirect You back to show barcode Activity.

Button *Delete* is used to permanently delete the store from the database, after clicking on this button, the store will be deleted and You'll
be redirected to the Home Activity.

<p align="center">
<img src="https://user-images.githubusercontent.com/92214769/173572131-0b05c832-e787-4bee-acfc-84cac8ffeb21.jpg" alt="drawing" width="150"/> <img src="https://user-images.githubusercontent.com/92214769/173572218-e78bd319-9b70-4dc7-b6ef-f714f43b4b85.jpg" alt="drawing" width="150"/> <img src="https://user-images.githubusercontent.com/92214769/173572245-e3ccd851-91fd-422c-95d3-8a6c31946d2b.jpg" alt="drawing" width="150"/>
</p>
