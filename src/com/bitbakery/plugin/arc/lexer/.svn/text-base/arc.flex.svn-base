/***** JFlex specification for Arc *****/

package com.bitbakery.plugin.arc.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import static com.bitbakery.plugin.arc.lexer.ArcTokenTypes.*;

%%

%class _ArcLexer
%implements FlexLexer
%unicode
%public

%function advance
%type IElementType



/***** Custom user code *****/

%{
    StringBuffer string = new StringBuffer();
%}




/***** Macros - Regular expressions *****/

LineTerminator=\r|\n|\r\n
InputCharacter=[^\r\n]
WhiteSpace={LineTerminator}|[ \t\f]

LineComment=([ \t\f]*";"{InputCharacter}*{LineTerminator})
CommentContent = ( [^*] | \*+ [^/*] )*
BlockComment = "#\|"{CommentContent}"\|#"


Digit=[0-9]
IntegerLiteral=(0|([1-9]({Digit})*))

ExponentPart=[Ee]["+""-"]?({Digit})*
FloatingPointLiteral1=({Digit})+"."({Digit})*({ExponentPart})?
FloatingPointLiteral2="."({Digit})+({ExponentPart})?
FloatingPointLiteral3=({Digit})+({ExponentPart})
FloatingPointLiteral4=({Digit})+
FloatLiteral=({FloatingPointLiteral1})|({FloatingPointLiteral2})|({FloatingPointLiteral3})|({FloatingPointLiteral4})

NumericLiteral=["+""-"]?({IntegerLiteral})|({FloatLiteral})

/**** TODO - Do we need support for pipe-delimited symbols, like in Common Lisp??? ********************/
/**** TODO - Is there some nice way to generalize this to other languages with non-Roman chars?? ******/
/**** TODO - Can we intercept special characters before we intercept symbols? ******/
Char=[A-Za-z0-9!@#$%<>_/?&\^\+\*\-=\.\?\;\|]
CharLiteral=#\\(newline|space|tab|return|\"|{Char})

Symbol={Char}*

EscapeSequence=\\[^\r\n]
StringLiteral=\"([^\\\"]|{EscapeSequence})*(\"|\\)?

/**** TODO - Include character literals ******************************/

%%

{WhiteSpace}     { return WHITESPACE; }
{CharLiteral}    { return CHAR_LITERAL; }
{LineComment}    { return LINE_COMMENT; }
{BlockComment}   { return BLOCK_COMMENT; }
{NumericLiteral} { return NUMERIC_LITERAL; }
{StringLiteral}  { return STRING_LITERAL; }


/** TODO - Any other tokens? =, +, etc.? *******************/

"("             { return LEFT_PAREN; }
")"             { return RIGHT_PAREN; }

"["             { return LEFT_SQUARE; }
"]"             { return RIGHT_SQUARE; }

/** TODO - We're not handling the underscore correctly. We probably need to do the whole JFlex-state-while-in-square-brackets thingy...  ******/
/** " _ "             { return UNDERSCORE; }  *****/


/** TODO - Anything special for empty lists? They *are* equivalent to nil, y'know... *************************/
[Tt]            { return TRUE; }
[Nn][Ii][Ll]    { return NIL; }

[Qq][Uu][Oo][Tt][Ee]        { return QUOTE_KEYWORD; }
[Ff][Nn]                    { return FN; }
[Ii][Ff]                    { return IF; }
[Dd][Oo]                    { return DO; }
[Ll][Ee][Tt]                { return LET; }
[Ww][Ii][Tt][Hh]            { return WITH; }

"~"             { return TILDE; }
"="             { return EQ; }
"`"             { return BACKQUOTE; }
"'"             { return QUOTE; }
","             { return COMMA; }
",@"            { return COMMA_AT; }
"."             { return DOT; }
":"             { return COMPOSER; }

[Dd][Ee][Ff]    { return DEF; }
[Mm][Aa][Cc]    { return MAC; }

/** TODO - In general, I think we'll want tokens for anything for which we'd like parsing - if statements, etc.)  ***/

/** TODO - In here is where we'd insert core stuff from ac.scm, as.scm, and arc.arc *************************/

/** TODO - In here is where we *might* choose to insert library stuff, although I don't know if that's *quite* to the "token" level... dunno, maybe iterators, at least **********/

{Symbol}        { return SYMBOL; }

.               { return BAD_CHARACTER; }