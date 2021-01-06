def call() {
    pipeline {
        agent any
        environment { 
            USER_NAME = 'Luis Varas Quinteros'
        }
        parameters {
            choice(name:'CHOICE', choices:['gradle','maven'], description: 'Elección de herramienta de construcción')
            string(name:'stages', defaultValue:'', description:'Elija')
        }
        stages {
            stage('Pipeline') {
                steps {
                    script {
                        figlet params.CHOICE
                        figlet 'Variables de Entorno'
                        
                        bat 'set'
                        def pipelineJob=GIT_BRANCH
                        figlet pipelineJob   
                        if (params.CHOICE == 'gradle')
                        {
                            gradle.call(params.CHOICE,params.stages, pipelineJob)
                        }
                        else
                        {
                            maven "${params.stages}"
                        }
                    }
                }
            }
        }
        /*
        post {
            
            success {
                slackSend channel: 'U01E2R4SXRN', 
                color: 'good', 
                message: "[${USER_NAME}] [${JOB_NAME}] [${params.CHOICE}] Ejecución exitosa", 
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
        */
    }
}

return this;
