import groovy.json.JsonOutput

@SuppressWarnings("GroovyAssignabilityCheck")
def checkoutAndMerge() {
    //noinspection GroovyAssignabilityCheck
    checkout changelog: true, poll: true, scm: [
            $class                           : 'GitSCM',
            branches                         : [[name: "origin/${env.gitlabSourceBranch}"]],
            doGenerateSubmoduleConfigurations: false,
            extensions                       : [[$class : 'PreBuildMerge',
                                                 options: [
                                                         fastForwardMode: "${env.GitSCM_fastForwardMode}",
                                                         mergeRemote    : 'origin',
                                                         mergeStrategy  : 'RESOLVE',
                                                         mergeTarget    : "${env.gitlabTargetBranch}"
                                                 ]],
                                                [$class: 'UserIdentity', email: "${env.GitSCM_UserIdentity_Email}", name: "${env.GitSCM_UserIdentity_Name}"]],
            submoduleCfg                     : [],
            userRemoteConfigs                : [[credentialsId: "${env.GitSCM_CredentialsId}",
                                                 name         : 'origin',
                                                 url          : "${env.gitlabSourceRepoHomepage}"]]
    ]
}

def checkout() {
    //noinspection GroovyAssignabilityCheck
    checkout changelog: true, poll: true, scm: [
            $class                           : 'GitSCM',
            branches                         : [[name: "${env.gitlabAfter}"]],
            doGenerateSubmoduleConfigurations: false,
            extensions                       : [[$class: 'UserIdentity', email: "${env.GitSCM_UserIdentity_Email}", name: "${env.GitSCM_UserIdentity_Name}"]],
            submoduleCfg                     : [],
            userRemoteConfigs                : [[credentialsId: "${env.GitSCM_CredentialsId}",
                                                 name         : 'origin',
                                                 url          : "${env.gitlabSourceRepoHomepage}"]]
    ]
}

def toJSONString(data) {
  return JsonOutput.toJson(data)
}



return [
        checkout:           this.&checkout,
        checkoutAndMerge:   this.&checkoutAndMerge,
        toJSONString:       this.&toJSONString,
]
