
Carry out the following steps:


1. Ensure that LaTeX2e, biblatex, and biber are installed on your system
   and pdflatex and biber are in your path.

   For Windows, I use cygwin and TexLive 2015 (ISO image).



2. Install the following packages, if they are not already part of
   your LaTeX2e installation:

     relsize
     titlesec
     csquotes
     datetime
     fmtcount
     verbdef
     etoolbox
     makecmds
     url

   With TexLive you can use the TexLive Manager GUI to install
   these online from a nearby ctan server.



3. Upgrade to the most recent versions of biblatex and biber.
   I use biblatex 3.3 and biber 2.4.

   With TexLive you can use the TexLive Manager GUI to update
   these online from a nearby ctan server.



4. Set the following environment variables:

   setenv TEXINPUTS .:~/tex/inputs:./inputs::
   setenv BSTINPUTS .:~/tex/inputs::
   setenv BIBINPUTS .:~/tex/bib:./bib::



5. Compile the PDF with the following sequence of commands

   pdflatex survey
   biber survey
   pdflatex survey
   pdflatex survey


  Alternatively, you can use the latexmk perl script:

   latexmk --pdf survey


