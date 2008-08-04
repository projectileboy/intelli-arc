Arc plugin for IntelliJ IDEA 7.0+
Version 0.4 - 2008/05/13
----------------------------------------------------------------------------------------

Requirements
------------

- MzScheme 352 or 360 (or any version which runs Arc)
  - Available at http://plt-scheme/mzscheme
- Arc
  - See http://www.arclanguage.org/install for details
- IntelliJ 7.0 or higher
  - Tested on Windows, Mac OS X 10.4, and Ubuntu 7.10



Quick Start
-----------

To get started, install the plugin, and specify your MzScheme and Arc home
directories. Then open up (or create) a *.arc file, and begin playing.


Changes
-------

0.4 (2008/05/13):
- Added support for CL-style docstrings; when available, these show up as documentation
- Added quick navigation via cmd-N - this is one of personal favorites thus far
- Added navigation from variable references to variable definitions, which includes defs and macs



Features
--------

- Simple syntax highlighting, with customizable fonts and colors
- Arc REPL
- Run selected code (option-R on the Mac, although this is customizable)
- Load current file  (option-L on the Mac, although this is customizable)
- Hot key to move focus to the REPL (option-G on the Mac, although this is customizable)
- Simple structure view, showing function and macro definitions in the current file,
    as well as variable assignments and anonymous function definitions

- TODO:
  - Rename refactoring
  - Move function/macro refactoring
  - Change single-variable
  - Extract anonymous function refactoring


Some nice stuff comes for free from IntelliJ:

- Local history of changes
- Integration with source control systems
- "TODO" lists automatically generated from your code
- Code folding



Known Issues
------------
- Find Usages is pretty well broken. I'm workin on it...
- The structure view now includes more, but looks a little noisy. I'm workin' on it... 
- The lexer is messing up block comments, betraying my sad ignorance of regexes.
- Abnormal termination from debug mode will leave MzScheme running, quickly eating all your RAM.
- The REPL doesn't currently support dragging and dropping text from the editor.
- Localizations are still in progress, and they're just hacks, built with Google Translate.
- ...and much, much more!



Bug Reporting
-------------

Comments, issues, bugs or feature requests should be posted at the website
for this plugin:

http://code.google.com/p/intelli-arc/



And now a word from our sponsor...
----------------------------------

Thank you for trying out this pre-pre-pre-alpha version of the Arc plugin
for IntelliJ IDEA, versions 7 and higher. My goal is to create a pleasant
development environment for writing Arc code. In particular, I'm trying to
build something for people like me - "blub" programmers who want to take a
step into Lisp, without losing all of the nice IDE features to which we've
become accustomed (e.g., built-in support for working with source control).
I've been inspired tremendously by the Cusp project, which is a lovely
Common Lisp plugin for Eclipse.



Thanks in advance for your patience and your feedback,
Kurt Christensen