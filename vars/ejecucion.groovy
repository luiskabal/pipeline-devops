def call() {
    pipeline {
       agent any
        // agent { dockerfile true }
        environment { 
            USER_NAME = 'Luis Varas Quinteros'
            GROUP = 'Grupo4'
            dockerImage = ''
            imagename = "devops/2021/diplomado/usach"
        }
        parameters {
            //choice(name:'CHOICE', choices:['gradle','maven'], description: 'Eleccion de herramienta de construccion')
            choice(name:'CHOICE', choices:['maven'], description: 'Eleccion de herramienta de construccion')
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
                  figlet "success"
                 /*slackSend channel: 'U01DK543PKN', 
                color: 'good', 
                message: "[${GROUP}] [${JOB_NAME}] [${params.CHOICE}] ["+env.NombreStage+"] Ejecucion exitosa", 
                teamDomain: 'dipdevopsusach2020', 
                tokenCredentialId: 'slack-Token'*/
                
            }
            failure {

                   figlet "failure"
                /*   slackSend channel: 'U01DK543PKN', 
                color: 'danger', 
                message: "ERROR EN ["+env.NombreStage+"]", 
                teamDomain: 'dipdevopsusach2020', 
                tokenCredentialId: 'slack-Token'*/
             
            }
        }
        
    }
}

return this;
