<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:xs="http://www.w3.org/2001/XMLSchema" 
                xmlns:doc="eml:documentation-2.0.0beta8" 
                version="1.0">
<xsl:output method="xml" indent="yes"/>
<xsl:output doctype-public="-//OASIS//DTD DocBook XML V4.1.2//EN" 
            doctype-system="http://www.oasis-open.org/docbook/xml/4.0/docbookx.dtd"/>

<xsl:template match="/">
<book>
  <bookinfo>
    <title>Guide to Ecological Metadata Language (EML)</title>
  </bookinfo>
  
  <toc>
    <tocpart>
      <tocchap>
        <tocentry><link linkend="preface">Preface</link></tocentry>
        <tocentry><link linkend="introduction">Introduction to EML</link></tocentry>
        <tocentry><link linkend="purpose">Purpose of EML</link></tocentry>
        <tocentry><link linkend="architecture">EML's Architecture</link></tocentry>
        <tocentry><link linkend="moduleDescriptions">Individual Module Descriptions (Non-normative)</link></tocentry>
        <tocentry><link linkend="index">Alphabetical Index of Elements</link></tocentry>
      </tocchap>
    </tocpart>
  </toc>
  
  <preface id="preface">
    <title>EML Overview &amp; History</title> 
    <section id="introduction">
      <title>Introduction</title>
      <para>
       Ecological Metadata Language (EML) is a metadata standard developed by the 
       ecology discipline and for the ecology discipline. It is based on prior 
       work done by the Ecological Society of America and associated efforts 
       (Michener et al., 1997, Ecological Applications). EML is implemented as 
       a series of XML document types that can by used in a modular and extensible 
       manner to document ecological data. Each EML module is designed to describe 
       one logical part of the total metadata that should be included with any 
       ecological dataset.
      </para>
    </section>
    
    <section id="purpose">
      <title>Purpose Statement</title>
      <para>
        To provide the ecological community with an extensible, flexible,
        metadata standard for use in data analysis and archiving that will 
        allow automated machine processing, searching and retrieval.  
      </para>
    </section>
    
    <section id="architecture">
      <title>Architecture</title>
      <para>
        The architecture of EML was designed to serve the needs of the
	ecological community, and has benefitted from previous work in other
	related metadata languages. EML has adopted the strengths of many of
	these languages, but also addresses a number of short-comings that
	have proved to inhibit the automated processing iand integration of 
	dataset resources via their metadata.
      </para>
      <para>
        The following list represents some of the features of EML:
      </para>
      <itemizedlist>
        <listitem>
	  Modularity: EML was designed as a collection of modules rather than
	  one large standard to facilitate future growth of the language in both
	  breadth and depth.  By implementing EML with an extensible
	  architecture, groups may choose which of the core modules are
	  pertinent to describing their data, literature, and software
	  resources.  Also, if EML falls short in a particular area, it may be
	  extended by creating a new module that describes the resource (e.g. a
	  detailed soils metadata profile that extends eml-dataset).  The intent
	  is to provide a common set of core modules for information exchange,
	  but to allow for futute customizations of the language without the
	  need of going through a lengthy 'approval' process.
        </listitem>
        <listitem>
	  Detailed Structure: EML strives to balance the tradeoff of 'too much
	  detail' with enough detail to enable advanced services in terms of
	  processing data through the parsing of accompanied metadata.
	  Therefore, a driving question throughout the design was: 'Will this
	  particular piece of information be machine-processed, just human
	  readable, or both?'  Information was then broken down into more highly
	  structured elements when the answer involved machine processing.
        </listitem>
        <listitem>
	  Compatibility: EML adopts much of it's syntax from the other metadata
	  standards that have evolved from the expertise of groups in other
	  disciplines.  Whenever possible, EML adopted entire trees of
	  information in order to facilitate conversion of EML documents into
	  other metadata languages.  EML was designed with with the following
	  standards in mind: Dublin Core Metadata Initiative, the Content
	  Standard for Digital Geospatial Metadata (CSDGM from the US geological
	  Survey's Federal Geographic Data Committee (FGDC)), the Biological
	  Profile of the CSDGM (from the National Biological Information
	  Infrastructure), the International Standards Organization's Geographic
	  Information Standard (ISO 19115), the ISO 8601 Date and Time Standard,
	  the OpenGIS Consortiums's Geography Markup Language (GML), the
	  Scientific, Technical, and Medical Markup Language (STMML), and the
	  Extensible Scientific Interchange Language (XSIL).
        </listitem>
        <listitem>
	  Strong Typing: EML is implemented in an Extensible Markup Language
	  (XML) known as XML Schema, which is a language that defines the rules
	  that govern the EML syntax.  XML Schema is an internet recommendation
	  from the World Wide Web Consortium (http://www.w3.org), and so a
	  metadata document that is said to comply with the syntax of EML will
	  structurally meet the criteria defined in the XML Schema documents for
	  EML.  Over and above the structure (what elements can be nested within
	  others, how many, etc.), XML Schema provides the ability to use strong
	  data typing within elements.  This allows for finer validation of the
	  contents of the element, not just it's structure.  For instance, an
	  element may be of type 'date', and so the value that is inserted in
	  the field will be checked against XML Schema's definition of a date.
	  Traditionally, XML documents have been validated against Document Type
	  Definitions (DTDs), which do not provide a means to employ strong
	  validation on field values through typing.  EML is also distributed
	  with DTD's that are generated from the XML Schema documents to provide
	  some backward compatability.
        </listitem>
      </itemizedlist>
    </section>
  </preface>
  
  <chapter id="moduleDescriptions">
    <title>Module Descriptions</title>
    <xsl:for-each select="//doc:module">
      <xsl:apply-templates select="document(.)//doc:moduleDocs"/>
    </xsl:for-each>
  </chapter>
  
  <index id="index">
    <title>Index</title>
      <indexdiv>
        <title>A</title>
        <xsl:for-each select="//doc:module">
          <xsl:for-each select="document(.)//xs:element">
            <xsl:sort select="@name" data-type="text"/>
            <xsl:if test="starts-with(@name, 'a')">
              <xsl:apply-templates select="." mode="indexentry"/>
            </xsl:if>
          </xsl:for-each>
        </xsl:for-each>
      </indexdiv>
      <indexdiv>
        <title>B</title>
        <xsl:for-each select="//doc:module">
          <xsl:for-each select="document(.)//xs:element">
            <xsl:if test="starts-with(./@name, 'b')">
              <xsl:apply-templates select="." mode="indexentry"/>
            </xsl:if>
          </xsl:for-each>
        </xsl:for-each>
      </indexdiv>
      <indexdiv>
        <title>C</title>
        <xsl:for-each select="//doc:module">
          <xsl:for-each select="document(.)//xs:element">
            <xsl:if test="starts-with(./@name, 'c')">
              <xsl:apply-templates select="." mode="indexentry"/>
            </xsl:if>
          </xsl:for-each>
        </xsl:for-each>
      </indexdiv>
      <indexdiv>
        <title>D</title>
        <xsl:for-each select="//doc:module">
          <xsl:for-each select="document(.)//xs:element">
            <xsl:if test="starts-with(./@name, 'd')">
              <xsl:apply-templates select="." mode="indexentry"/>
            </xsl:if>
          </xsl:for-each>
        </xsl:for-each>
      </indexdiv>
      <indexdiv>
        <title>E</title>
        <xsl:for-each select="//doc:module">
          <xsl:for-each select="document(.)//xs:element">
            <xsl:if test="starts-with(./@name, 'e')">
              <xsl:apply-templates select="." mode="indexentry"/>
            </xsl:if>
          </xsl:for-each>
        </xsl:for-each>
      </indexdiv>
      <indexdiv>
        <title>F</title>
        <xsl:for-each select="//doc:module">
          <xsl:for-each select="document(.)//xs:element">
            <xsl:if test="starts-with(./@name, 'f')">
              <xsl:apply-templates select="." mode="indexentry"/>
            </xsl:if>
          </xsl:for-each>
        </xsl:for-each>
      </indexdiv>
      <indexdiv>
        <title>G</title>
        <xsl:for-each select="//doc:module">
          <xsl:for-each select="document(.)//xs:element">
            <xsl:if test="starts-with(./@name, 'g')">
              <xsl:apply-templates select="." mode="indexentry"/>
            </xsl:if>
          </xsl:for-each>
        </xsl:for-each>
      </indexdiv>
      <indexdiv>
        <title>H</title>
        <xsl:for-each select="//doc:module">
          <xsl:for-each select="document(.)//xs:element">
            <xsl:if test="starts-with(./@name, 'h')">
              <xsl:apply-templates select="." mode="indexentry"/>
            </xsl:if>
          </xsl:for-each>
        </xsl:for-each>
      </indexdiv>
      <indexdiv>
        <title>I</title>
        <xsl:for-each select="//doc:module">
          <xsl:for-each select="document(.)//xs:element">
            <xsl:if test="starts-with(./@name, 'i')">
              <xsl:apply-templates select="." mode="indexentry"/>
            </xsl:if>
          </xsl:for-each>
        </xsl:for-each>
      </indexdiv>
      <indexdiv>
        <title>J</title>
        <xsl:for-each select="//doc:module">
          <xsl:for-each select="document(.)//xs:element">
            <xsl:if test="starts-with(./@name, 'j')">
              <xsl:apply-templates select="." mode="indexentry"/>
            </xsl:if>
          </xsl:for-each>
        </xsl:for-each>
      </indexdiv>
      <indexdiv>
        <title>k</title>
        <xsl:for-each select="//doc:module">
          <xsl:for-each select="document(.)//xs:element">
            <xsl:if test="starts-with(./@name, 'k')">
              <xsl:apply-templates select="." mode="indexentry"/>
            </xsl:if>
          </xsl:for-each>
        </xsl:for-each>
      </indexdiv>
      <indexdiv>
        <title>L</title>
        <xsl:for-each select="//doc:module">
          <xsl:for-each select="document(.)//xs:element">
            <xsl:if test="starts-with(./@name, 'l')">
              <xsl:apply-templates select="." mode="indexentry"/>
            </xsl:if>
          </xsl:for-each>
        </xsl:for-each>
      </indexdiv>
      <indexdiv>
        <title>M</title>
        <xsl:for-each select="//doc:module">
          <xsl:for-each select="document(.)//xs:element">
            <xsl:if test="starts-with(./@name, 'm')">
              <xsl:apply-templates select="." mode="indexentry"/>
            </xsl:if>
          </xsl:for-each>
        </xsl:for-each>
      </indexdiv>
      <indexdiv>
        <title>N</title>
        <xsl:for-each select="//doc:module">
          <xsl:for-each select="document(.)//xs:element">
            <xsl:if test="starts-with(./@name, 'n')">
              <xsl:apply-templates select="." mode="indexentry"/>
            </xsl:if>
          </xsl:for-each>
        </xsl:for-each>
      </indexdiv>
      <indexdiv>
        <title>O</title>
        <xsl:for-each select="//doc:module">
          <xsl:for-each select="document(.)//xs:element">
            <xsl:if test="starts-with(./@name, 'o')">
              <xsl:apply-templates select="." mode="indexentry"/>
            </xsl:if>
          </xsl:for-each>
        </xsl:for-each>
      </indexdiv>
      <indexdiv>
        <title>P</title>
        <xsl:for-each select="//doc:module">
          <xsl:for-each select="document(.)//xs:element">
            <xsl:if test="starts-with(./@name, 'p')">
              <xsl:apply-templates select="." mode="indexentry"/>
            </xsl:if>
          </xsl:for-each>
        </xsl:for-each>
      </indexdiv>
      <indexdiv>
        <title>Q</title>
        <xsl:for-each select="//doc:module">
          <xsl:for-each select="document(.)//xs:element">
            <xsl:if test="starts-with(./@name, 'q')">
              <xsl:apply-templates select="." mode="indexentry"/>
            </xsl:if>
          </xsl:for-each>
        </xsl:for-each>
      </indexdiv>
      <indexdiv>
        <title>R</title>
        <xsl:for-each select="//doc:module">
          <xsl:for-each select="document(.)//xs:element">
            <xsl:if test="starts-with(./@name, 'r')">
              <xsl:apply-templates select="." mode="indexentry"/>
            </xsl:if>
          </xsl:for-each>
        </xsl:for-each>
      </indexdiv>
      <indexdiv>
        <title>S</title>
        <xsl:for-each select="//doc:module">
          <xsl:for-each select="document(.)//xs:element">
            <xsl:if test="starts-with(./@name, 's')">
              <xsl:apply-templates select="." mode="indexentry"/>
            </xsl:if>
          </xsl:for-each>
        </xsl:for-each>
      </indexdiv>
      <indexdiv>
        <title>T</title>
        <xsl:for-each select="//doc:module">
          <xsl:for-each select="document(.)//xs:element">
            <xsl:if test="starts-with(./@name, 't')">
              <xsl:apply-templates select="." mode="indexentry"/>
            </xsl:if>
          </xsl:for-each>
        </xsl:for-each>
      </indexdiv>
      <indexdiv>
        <title>U</title>
        <xsl:for-each select="//doc:module">
          <xsl:for-each select="document(.)//xs:element">
            <xsl:if test="starts-with(./@name, 'u')">
              <xsl:apply-templates select="." mode="indexentry"/>
            </xsl:if>
          </xsl:for-each>
        </xsl:for-each>
      </indexdiv>
      <indexdiv>
        <title>V</title>
        <xsl:for-each select="//doc:module">
          <xsl:for-each select="document(.)//xs:element">
            <xsl:if test="starts-with(./@name, 'v')">
              <xsl:apply-templates select="." mode="indexentry"/>
            </xsl:if>
          </xsl:for-each>
        </xsl:for-each>
      </indexdiv>
      <indexdiv>
        <title>W</title>
        <xsl:for-each select="//doc:module">
          <xsl:for-each select="document(.)//xs:element">
            <xsl:if test="starts-with(./@name, 'w')">
              <xsl:apply-templates select="." mode="indexentry"/>
            </xsl:if>
          </xsl:for-each>
        </xsl:for-each>
      </indexdiv>
      <indexdiv>
        <title>X</title>
        <xsl:for-each select="//doc:module">
          <xsl:for-each select="document(.)//xs:element">
            <xsl:if test="starts-with(./@name, 'x')">
              <xsl:apply-templates select="." mode="indexentry"/>
            </xsl:if>
          </xsl:for-each>
        </xsl:for-each>
      </indexdiv>
      <indexdiv>
        <title>Y</title>
        <xsl:for-each select="//doc:module">
          <xsl:for-each select="document(.)//xs:element">
            <xsl:if test="starts-with(./@name, 'y')">
              <xsl:apply-templates select="." mode="indexentry"/>
            </xsl:if>
          </xsl:for-each>
        </xsl:for-each>
      </indexdiv>
      <indexdiv>
        <title>Z</title>
        <xsl:for-each select="//doc:module">
          <xsl:for-each select="document(.)//xs:element">
            <xsl:if test="starts-with(./@name, 'z')">
              <xsl:apply-templates select="." mode="indexentry"/>
            </xsl:if>
          </xsl:for-each>
        </xsl:for-each>
      </indexdiv>
  </index>
</book>
</xsl:template>

<xsl:template match="doc:moduleDocs">
<section>
  <xsl:attribute name="id">
    <xsl:value-of select="./doc:moduleName"/>
  </xsl:attribute>
  <title><xsl:value-of select="./doc:moduleName"/></title>
  <itemizedlist>
    <listitem>
      <para>Recommended Usage: <xsl:value-of select="normalize-space(./doc:recommendedUsage)"/></para>
    </listitem>
    <listitem>
      <para>Stand-alone: <xsl:value-of select="normalize-space(./doc:standAlone)"/></para>
    </listitem>
    <xsl:if test="count(./doc:importedBy) > 0">
    <listitem>
      <para>Imported By:</para> 
      <itemizedlist><xsl:apply-templates select="./doc:importedBy"/></itemizedlist>
    </listitem>
    </xsl:if>
    <xsl:if test="count(./doc:relationship) > 0">
    <listitem>
      <para>Relates To:</para>
      <segmentedlist>
        <segtitle>related module</segtitle>
        <segtitle>relationship</segtitle>
        <xsl:apply-templates select="./doc:relationship"/>
      </segmentedlist>
    </listitem>
    </xsl:if>
    <listitem>
      <para>
        <ulink>
          <xsl:attribute name="url">./<xsl:value-of select="./doc:moduleName"/>.html</xsl:attribute>
          Technical Specifications
        </ulink>
      </para>
    </listitem>
  </itemizedlist>
  <para>
    <xsl:value-of select="./doc:moduleDescription"/>
  </para>
</section>
</xsl:template>

<xsl:template match="doc:relationship">
  <seglistitem>
    <seg><xsl:value-of select="normalize-space(./doc:moduleName)"/></seg>
    <seg><xsl:value-of select="normalize-space(./doc:relationshipType)"/></seg>
  </seglistitem>
</xsl:template>

<xsl:template match="doc:importedBy">
  <listitem>
    <para>
      <xsl:value-of select="normalize-space(.)"/>
    </para>
  </listitem>
</xsl:template>

<xsl:template match="xs:element" mode="indexentry">
  <indexentry>
    <primaryie>
      <ulink>
        <xsl:attribute name="url">./<xsl:value-of select="//doc:moduleName"/>.html#<xsl:value-of select="@name"/></xsl:attribute>
        <xsl:value-of select="@name"/></ulink>-<xsl:value-of select="//doc:moduleName"/>
    </primaryie>
  </indexentry>
</xsl:template>
</xsl:stylesheet>
