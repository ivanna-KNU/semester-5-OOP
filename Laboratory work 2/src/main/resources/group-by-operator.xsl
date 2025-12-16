<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" indent="yes" encoding="UTF-8"/>

    <xsl:key name="plans-by-operator" match="Tariff/plans/plan" use="@operatorRef"/>

    <xsl:template match="/">
        <tariffsByOperator>
            <xsl:for-each select="Tariff/plans/plan[generate-id() = generate-id(key('plans-by-operator', @operatorRef)[1])]">
                <xsl:variable name="opId" select="@operatorRef"/>
                <operator>
                    <xsl:attribute name="id"><xsl:value-of select="$opId"/></xsl:attribute>
                    <name>
                        <xsl:value-of select="Tariff/operators/operator[@id=$opId]/name"/>
                    </name>
                    <plans>
                        <xsl:for-each select="key('plans-by-operator', $opId)">
                            <plan>
                                <xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
                                <xsl:copy-of select="name | payroll | callPrices | smsPrice | parameters"/>
                            </plan>
                        </xsl:for-each>
                    </plans>
                </operator>
            </xsl:for-each>
        </tariffsByOperator>
    </xsl:template>

</xsl:stylesheet>
