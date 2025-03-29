@Library('shared-lib') _

myPipeline(
        repoUrl: 'https://bitbucket.org/codemaniac-ray/appt-mgmt-service.git',
        branch: 'main',
        credentialsId: 'bitbucket-creds',
        projectKey: 'appt-mgmt-service'
)
