<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/">
        <html>
            <head>
                <title>
                    <xsl:value-of select="rss/channel/title"/> RSS Feed
                </title>
                <link href="rss.css" rel="stylesheet" type="text/css" />
            </head>
            <body>
                <div id="logo">
                    <a alt="{rss/channel/title}" href="{rss/channel/link}">
                        <img src="{rss/channel/image/url}" alt="{rss/channel/image/title}"/>
                    </a>
                </div>
                <div id="content">
                    <xsl:for-each select="rss/channel/item">
                        <div class="article">
                            <h2>
                                <a href="{link}" rel="bookmark">
                                    <xsl:value-of select="title"/>
                                </a>
                            </h2>
                            <xsl:value-of select="description"/>
                        </div>
                    </xsl:for-each>
                </div>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>