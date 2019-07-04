# Diary (console version)
This is a console version of the application **Diary**. It has reduced functionality and is designed to test database operations.<br>
**IDE**: Eclipse<br>
**Database**: Derby<br>
**Dependencies:** JDK
## Installing and running
If you want to build a project from source, put project folder in the workspace directory and open it in Eclipse IDE: **File -> Import... -> Existing Projects into Workspace**. Then you can run it or build .jar file using **File -> Export... -> Runnable JAR file**.<br><br>
If you do not want to build the project manually, just take the ready **diary.jar** archive.<br><br>
When you get the file, you can put it in any directory you want and run it from the command line:
```bash
java -jar diary.jar
```
In the directory where the program is located, a database will be automatically created. Its default name is *diary*, but you can use the command line argument to change it:
```bash
java -jar diary.jar summer_diary
```
## Using
A diary entry contains four fields: a unique identifier (ID), date, title and text. ID and date are set automatically when adding an entry and are not editable by the user. The user can add only the title (128 characters) and text (32672 characters).<br>
#### Available commands
* `add` - add a note.
* `all` - show all notes.
* `show [id]` - show specific note by id.
* `delete [id-id]` - delete notes by id in range [from, to].
* `delete [id {, id}]` - delete notes by id individually.
* `help` - get the list of commands.
* `exit` - leave the diary.
* `edit [id]` - edit full note (title and text) by id.
* `edit [id [title | text]]` - edit title or text of specific note by id.

Use `help` to see the command list in your terminal.<br>
#### Examples
1. Adding a note:
```
-> add
```
After that you will receive an invitation to enter data:
```
Title:
Hello Turkey!
Text:
Today I arrived in Cappadocia.
```
2. View all notes:
```
-> all
```
You will receive all the entries from the diary, for example:
```
ID   | DATE       | TITLE                | TEXT                          
-----+------------+----------------------+-------------------------------
1    | 2019-07-04 | Hello Turkey!        | Today I arrived in Cappadocia.
2    | 2019-07-05 | Istanbul Tour        | The caves of Cappadocia wer...
3    | 2019-07-07 | Holiday in Kemer     | I will spend a few days on ...
```
3. View a specific note:
```
-> show 3
```
You can use only the ID to open a specific note (find it in the ID column using `all`). Result (no word-wrap):
```
Id: 3
Date: 2019-07-07
Title: Holiday in Kemer
Text: I will spend a few days on the beach in Kemer. Maybe I will even go somewh
ere on a tour, for example, to the church of St. Nicholas. I'll think a little m
ore. I really want to climb Mount Tahtali. It offers a wonderful view from the h
otel.
```
4. Deleting entries (only by ID):
* in the range from N to M:
```
-> delete 4-7
```
* individually (several IDs):
```
-> delete 3,7,10
```
* individually (one ID):
```
-> delete 2
```
5. Editing a note (only by ID):
* fully:
```
-> edit 5
```
* only title:
```
-> edit 7 title
```
* only text:
```
-> edit 4 text
```
After that you will receive an invitation to enter new data.
6. Leave the diary:
```
-> exit
```






