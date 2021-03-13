import groovy.util.XmlSlurper

class ServiceList{
  static void main(String[] args){
    print "start running------------------------------- "
    def pomXMLStr = args[0] + ""
    def notModuleServiceList = 'cicd;commons;od/od-common-business;security-common;cr/cr-common-business;kedb/kedb-common-business;pt/pt-common-business;tt/tt-common-business;wo/wo-common-business;tracing'
    print pomXMLStr
    def serviceList = []
    File file = new File("GetServerList.txt")

    def xmlParser = new XmlSlurper()
    xmlParser.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false)
    xmlParser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
    def pomXml = xmlParser.parseText(pomXMLStr)
    print pomXml

    pomXml.modules[0].module.each {modu ->
      boolean isService = true
      def notServiceModuleList1 = "${notModuleServiceList}".split(";")
      for (notServiceModule in notServiceModuleList1) {
        println "Check module ${notServiceModule}"
        if (modu== notServiceModule) {
          isService = false
        }
      }
      if (isService){
        println "module is service: ${modu}"
        file << "${modu},"
      }
    }
    print file.text
  }

}
