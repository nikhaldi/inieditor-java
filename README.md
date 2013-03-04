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


Installation
------------

Put the file `lib/inieditor.jar` on your classpath. Import the
`IniEditor` class with the statement:

    import com.nikhaldimann.inieditor.IniEditor;

Alternatively, you can compile the sources in the `src` directory
yourself.


Documentation
-------------

Open the file `javadoc/index.html` in a browser for the
Javadoc-generated documentation.


Changelog
---------

r1 (07/31/2003):
- initial public release

r2 (12/20/2003):
- added methods load(InputStreamReader) and save(OutputStreamWriter) to
  support character encodings other than the default one through readers
  and writers.

r3 (5/4/2005):
- fixed bug where loaded files weren't closed properly.

r4 (8/10/2005):
- added the concept of an option format which makes it possible to
  finetune the output format when saving to a file.

r5 (3/4/2013):
- revived the code from an old backup
- renamed package from `ch.ubique.inieditor` to `com.nikhaldimann.inieditor`,
  as I don't own the ubique.ch domain anymore

License
-------

IniEditor is Copyright (c) 2003-2013, Nik Haldimann (nhaldimann at gmail dot com).
It comes with a BSD-style license: You may use and modify it freely as well as
redistribute it under certain conditions. See the `LICENSE.txt` file
for details.
