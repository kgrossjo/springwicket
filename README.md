# Spring Wicket

The project name is a misnomer, there is no Apache Wicket in it (yet?).

I'm trying to learn how to develop a web application in Java using
some standard modern framework.

Originally, I wanted to create a web app using Apache Wicket.  Then I
discovered that Wicket only does the views but I needed controllers
and a persistence layer, too.  I spent a couple of hours on this phase.

So I turned to Spring Boot, which gave me something using JPA and Hibernate
and Spring MVC and Thymeleaf.  I liked Spring Boot because it told
me that it's an opinionated collection of various frameworks.  This is
good because I don't know the Spring ecosystem (yet) and therefore
I'm happy to go with whatever someone suggests.  Once I have a bit of
experience, I can swap different parts out.

After trying Spring Boot, I hit another brick wall: Spring Boot suggests
to use the H2 database as an in-memory database.  But I don't want the
documents I've entered to disappear when I have to restart the server.
So I tried MariaDB, but then I ran into the issue that the database
would hang after running the application once or twice.  So then I decided
to investigate other databases, and found out that H2 allows me to store
the data in files.  That seemed to be good, so I used that.  I guess it
took me two or three hours to overcome this road block.

From then on, it was fairly smooth sailing.

I switched back and forth between different tutorials and reading various
API descriptions, and the Thymeleaf documentation.  At the end, I added
a bit of UI polish using Bootstrap and learned about "webjars".

The whole thing took maybe 10 or 15 hours.

## What does the app do?

Considering my Information Retrieval background, the idea is this:
You are able to CRUD documents, where a document comprises a title
and some content.  The app will maintain an inverted index over these
documents: Both title and content will be split into words, each word
will be converted to its stem form (using the Porter stemmer, this only
works for English).  Then we count how often each stem occurs in
each document.

You can also enter a search query (also a sequence of words).  The search
query will be split into words, stemmed, and counted just like a document
would.  Then we use the vector space model of IR (using tf*idf -- term 
frequency times inverted document frequency) to rank all the documents
against the query.
