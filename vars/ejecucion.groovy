def call() {
    pipeline {
        agent any
        environment { 
            USER_NAME = 'Luis Varas Quinteros'
            GROUP = 'Grupo4'
        }
        parameters {
            choice(name:'CHOICE', choices:['gradle','maven'], description: 'Eleccion de herramienta de construccion')
            string(name:'stages', defaultValue:'', description:'Ingrese las stages que desee utilizar en el pipeline')
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
                slackSend channel: 'U01DK543PKN', 
                color: 'good', 
                message: "[${GROUP}] [${JOB_NAME}] [${params.CHOICE}] "+env.NombreStage+" Ejecucion exitosa", 
                teamDomain: 'dipdevopsusach2020', 
                tokenCredentialId: 'slack-Token'
            }
            failure {
                slackSend channel: 'U01DK543PKN', 
                color: 'danger', 
                message: "ERROR EN "+env.NombreStage, 
                teamDomain: 'dipdevopsusach2020', 
                tokenCredentialId: 'slack-Token'
            }
        }
        
    }
}

return this;
