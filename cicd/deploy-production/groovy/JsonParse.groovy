import groovy.util.XmlSlurper

class ServiceList{
  static void main(String[] args){
    print "start running------------------------------- "
    def pomXMLStr = args[0] + ""
    def notModuleServiceList = 'cicd;commons;od/od-common-business;security-common;cr/cr-common-business;kedb/kedb-common-business;pt/pt-common-business;tt/tt-common-business;wo/wo-common-business;cr/cr-category-service;cr/cr-outside-service;cr/cr-service;documentation-service;kedb/kedb-service;kedb/kedb-outside-service;pt/pt-service;pt/pt-category-service;pt/pt-outside-service;tt/tt-service;tt/tt-category-service;tt/tt-outside-service;wo/wo-category-service;wo/wo-outside-service;wo/wo-service;wo/wo-vsmart-service'
    print pomXMLStr
    def serviceList = []
    File file = new File("JsonParse.txt")

    def xmlParser = new XmlSlurper()
    xmlParser.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false)
    xmlParser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
    def pomXml = xmlParser.parseText(pomXMLStr)
    print pomXml


    print file.text
  }

}
