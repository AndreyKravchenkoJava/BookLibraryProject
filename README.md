**Book Library**

The service for tracking borrowed books and book readers (borrowers).

**Main terms**

    • Book – just a book. 
    • Reader – a person who can borrow a book for reading(borrower)

**Implementation**

This project supposed to be implemented in several steps or layers of complexity. On lower steps you need to know only basic java technologies, on higher steps you need to know more tools, libraries and frameworks.Main features should be implemented as a minimal requirement.

**Project’s goal**

The project allows a student to get somereal coding practice as soon as they have studied a new technology or tool.For example, on the 1stlevel of the project a student can use their basic knowledge of java core and the 1stlevel can be implemented during a “java core”course/book completion.The 2ndlevel adds more complexity in business logic, pushes a student to start thinking about the app’s architecture on a basic leveland, at the same time, the task doesn’t exceeds the java core scope.The 3rdlevelimplementationis possible only after the completion of JDBC topic studyand, at the same time, offers an entry level of complexity within JDBC topic.And so on.So, basically, youcan work on this project from your first steps in java up to your Java Junior skill level and this project can be one of your portfolio’s projects.

**Level 1**

Implement the service interacting with user via console. No real values input from user, only menu options. All answers are hardcoded. So, the service reads user’s input from console and prints the answers (responses) back in console.

**Required knowledge**

    • Java coreMain features    
    • Program’s entry point: show user menu. User menu offers several actions to choose from, with a short explanation. This is how your menu should look like(ignore the font and color, only the text is important):

WELCOME TO THE LIBRARY!PLEASE,SELECT ONE OF THE FOLLOWING ACTIONS BY TYPING THE OPTION’S NUMBER AND PRESSING ENTER KEY:

[1]SHOW ALL BOOKS IN THE LIBRARY

[2]SHOW ALL READERS REGISTERED IN THE LIBRARY

TYPE “EXIT” TO STOP THE PROGRAM AND EXIT!

    • When user types 1 and hit enter key –theprogram prints all existing books 
    • When user types 2 and hit enter –the program prints all registered readers•When user types “exit”and hit enter –the programs print “Goodbye!”and exits. 
    • Until user exit the program, after each action the program shows the menu and user can execute non-termination actions many times. 
    • Welcome line is shown only once, at program’s start. 
    • Book entity has the following fields: 
        o Id    
        o Name
        o Author 
    • Reader entity has the following fields: 
        o Id 
        o Name 
    • From the startup the library has 3 available books and 3 registered readers, feel free to generate any names you want