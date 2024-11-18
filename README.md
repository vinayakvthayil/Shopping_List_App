# Shopping List App 

## Project Description

This **Shopping List App** allows users to add, edit, and view items in their shopping list. The app provides a simple interface to manage shopping items, including the item's name, quantity, and any notes. The app is built using **Android Studio** and is developed in **Java**. The data is stored using **ContentProvider** and SQLite database.

### Features:
- **Add Item**: Allows users to add new items to the shopping list.
- **Edit Item**: Allows users to modify existing items in the shopping list.
- **View Items**: Displays a list of all the items with their name, quantity, and notes.
- **Delete Item**: Allows users to delete an item from the shopping list (not included in this code, but you can extend it).

## Screenshots
<table>
  <tr>
    <th>Home Page</th>
    <th>Edit Item</th>
    <th>After Editing List</th>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/51b8e32d-9fe5-43ea-903f-f81b6c8ff040" width="300" height="600"></td>
    <td><img src="https://github.com/user-attachments/assets/e44dd23e-f5c9-4321-b7ae-923c1c61e511" width="300" height="600"></td>
    <td><img src="https://github.com/user-attachments/assets/d80970a4-2945-49c8-bc5c-974cc8ac0b35" width="300" height="600"></td>
  </tr>
  <tr>
    <th>Added List</th>
    <th>Add Item</th>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/d19fecc5-348f-40d6-9d27-40a13ee3fc1f" width="300" height="600"></td>
    <td><img src="https://github.com/user-attachments/assets/7d13931a-cadc-41aa-9246-673e2c3866f7" width="300" height="600"></td>
  </tr>
</table>

## Prerequisites

- Android Studio (latest version recommended)
- Java (JDK 8 or higher)
- Android Emulator or an Android device for testing

---

## Setup Instructions

1. **Clone the Repository**:
   Clone this repository to your local machine using the following command:

   ```bash
   git clone https://github.com/vinayakvthayil/Shopping_List_App.git
   ```

2. **Open the Project in Android Studio**:
   - Open Android Studio.
   - Select **Open an existing project**.
   - Browse to the folder where you cloned the project and select it.

3. **Build and Run the Project**:
   - Click on the **Build** button .
   - Once the build is complete, you can run the app on the Android emulator or on a physical device.
   - Click the **Run** button to launch the app.

---

## App Workflow

- **Main Activity**: Displays a list of items and includes buttons to add and edit items.
- **Add/Edit Item Activity**: This screen allows the user to input details of the item (name, quantity, and notes). If an item is being edited, the fields are populated with the existing item details.
- **Database**: Data is stored in an SQLite database accessed through a `ContentProvider`.

### Core Features:
1. **Add Item**:
   - On the main screen, there is an "Add Item" button. Clicking it takes the user to the `AddEditItemActivity` where they can enter the item details.
   - If the item is new, the app adds it to the database.

2. **Edit Item**:
   - When the user clicks on an existing item in the list, they are taken to the `AddEditItemActivity`, where they can edit the details of the item.
   - After editing, the item is updated in the database.

3. **Save Data**:
   - When adding or editing an item, the data is saved in the database using `ContentValues`.

---

## Files and Code Structure

- **MainActivity.java**: Displays the list of items.
- **AddEditItemActivity.java**: Handles adding or editing items.
- **ShoppingListProvider.java**: Manages database access via content provider.
- **ShoppingListDbHelper.java**: Handles SQLite database creation and queries.
- **activity_add_edit_item.xml**: XML layout for adding or editing items.
- **activity_main.xml**: XML layout for displaying the shopping list.

---

## Dependencies

The app uses the following dependencies:

- **SQLite** for database management.
- **ContentProvider** for data access.
- **Android Support Libraries** for backward compatibility.
