def call() {
    pipeline {
        agent any
        environment { 
            USER_NAME = 'Luis Varas Quinteros'
            GROUP = 'Grupo4'
            STAGE_NAME=""
        }
        parameters {
            choice(name:'CHOICE', choices:['gradle','maven'], description: 'Eleccion de herramienta de construccion, buildAndTest')
            string(name:'stages', defaultValue:'', description:'Elija')
        }
        stages {
            stage('Pipeline') {
                steps {
                    script {
                        def pipelineJob=GIT_BRANCH
                        figlet pipelineJob   
                        if (params.CHOICE == 'gradle')
                        {
                            gradle.call(params.CHOICE,params.stages, pipelineJob)
                        }
                        else
                        {
                            maven.call(params.CHOICE,params.stages, pipelineJob)
                        }
                    }
                }
            }
        }
        
        post {
            
            success {
                slackSend channel: 'U01E2R4SXRN', 
                color: 'good', 
                message: "[${GROUP}] [${JOB_NAME}] [${params.CHOICE}] [${STAGE_NAME}]Ejecución exitosa", 
                teamDomain: 'dipdevopsusach2020', 
                tokenCredentialId: 'slack-token'
            }
            failure {
                slackSend channel: 'U01E2R4SXRN', 
                color: 'danger', 
                message: "ERROR EN ${STAGE_NAME}", 
                teamDomain: 'dipdevopsusach2020', 
                tokenCredentialId: 'slack-token'
            }
        }
        
    }
}

return this;
