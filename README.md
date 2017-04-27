IniEditor
=========

IniEditor is a small Java library to read and edit INI-style
configuration files. Its core feature is that it preserves the general
formatting of a loaded INI file: It represents comments, blank lines as
well as the order of sections and lines.


Example code
------------

Here's a minimal example. Suppose, we have this in a file called
`users.ini`:

    [root]
    role = administrator
    last_login = 2003-05-04

    [joe]
    role = author
    last_login = 2003-05-13

Let's load that file, add something to it and save the changes:

    import com.nikhaldimann.inieditor.IniEditor;

    IniEditor users = new IniEditor();
    users.load("users.ini");
    users.set("root", "last_login", "2003-05-16");
    users.addComment("root", "Must change password often");
    users.set("root", "change_pwd", "10 days");
    users.addBlankLine("root");
    users.save("users.ini");

This is how the file turned out:

    [root]
    role = administrator
    last_login = 2003-05-16

    # Must change password often
    change_pwd = 10 days

    [joe]
    role = author
    last_login = 2003-05-13


Documentation
-------------

The latest JavaDocs generated from the source are at
http://nikhaldi.github.com/inieditor-java/javadoc/


Downloads
---------

The latest release is available from
[Maven Central](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.nikhaldimann%22%20AND%20a%3A%22inieditor%22).
Add this dependency to your pom.xml:

    <dependency>
      <groupId>com.nikhaldimann</groupId>
      <artifactId>inieditor</artifactId>
      <version>r6</version>
    </dependency>

If you use a build system other than Maven, a download of the latest release including a precompiled JAR file is available at
http://nikhaldi.github.com/inieditor-java/downloads/inieditor.tar.gz

If for some reason you need an older release, r4 (from before the
package rename) is also available to download at
http://nikhaldi.github.com/inieditor-java/downloads/inieditor-r4.tar.gz


Changelog
---------

r1, 07/31/2003:
- initial public release

r2, 12/20/2003:
- added methods load(InputStreamReader) and save(OutputStreamWriter) to
  support character encodings other than the default one through readers
  and writers.

r3, 5/4/2005:
- fixed bug where loaded files weren't closed properly.

r4, 8/10/2005 ([download](http://nikhaldi.github.com/inieditor-java/downloads/inieditor-r4.tar.gz)):
- added the concept of an option format which makes it possible to
  finetune the output format when saving to a file.

r5, 3/4/2013 ([Maven](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.nikhaldimann%22%20AND%20a%3A%22inieditor%22), [download](http://nikhaldi.github.com/inieditor-java/downloads/inieditor-r5.tar.gz)):
- revived the code from an old backup
- renamed package from `ch.ubique.inieditor` to `com.nikhaldimann.inieditor`,
  as I don't own the ubique.ch domain anymore
- used generic collection types throughout

r6, 4/27/2017 ([Maven](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.nikhaldimann%22%20AND%20a%3A%22inieditor%22), [download](http://nikhaldi.github.com/inieditor-java/downloads/inieditor.tar.gz))
- added IniEditor.getSectionMap()


License
-------

IniEditor is Copyright (c) 2003-2017, Nik Haldimann (nhaldimann at gmail dot com).
It comes with a BSD-style license: You may use and modify it freely as well as
redistribute it under certain conditions. See the `LICENSE.txt` file
for details.
