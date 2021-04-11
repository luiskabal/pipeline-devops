package pipeline.test

def getValidatedStages(String type,String chosenStages, String pipelineJob) {
     def stages = []
     def pipelineStages=usedPipeline(type,pipelineJob)
    try {
   
    if (chosenStages?.trim()) {
        chosenStages.split(';').each{
            if (it in pipelineStages){
                stages.add(it)
            } else {
                env.FAIL_MESSAGE = "No existe el stage ${it}, por lo que no se pudo realizar la ejecución"
                error "${it} no existe como Stage. Stages disponibles para ejecutar: ${pipelineStages}"
            }
        }
        println "Validación de stages correcta. Se ejecutarán los siguientes stages en orden: ${stages}"
    } else {
        stages = pipelineStages
        println "Parámetro de stages vacío. Se ejecutarán todos los stages en el siguiente orden: ${stages}"
    }    
    }
    catch(Exception e) {
          figlet e
    }     

    return stages   
}
def usedPipeline(type,pipelineJob){
    def usedStages=[]
    if(pipelineJob.contains('release-')){
         figlet 'CD';
         //usedStages=['gitDiff','downloadNexus','runDownload','rest','gitMergeMaster','gitMergeDevelop','gitTagMaster','nexusCD']
         usedStages=['gitDiff','downloadNexus','runDownload','rest','gitMergeMaster','gitMergeDevelop','gitTagMaster']
    }else{
        figlet 'CI';
       // usedStages = type.contains('maven')?['compile','unitTest','Jar','Sonar','nexusCI']:['buildAndTest','sonar','runJar','rest','nexusCI']
         usedStages = type.contains('maven')?['compile','jar']:['buildAndTest','sonar','runJar','rest','nexusCI']
        
        if(pipelineJob.contains("develop")){
            usedStages.add('createRelease')
        }
    }

return usedStages
}

return this