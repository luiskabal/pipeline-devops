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
    if(pipelineJob.contains("release-")){
         figlet 'CD';
         usedStages=['downloadNexus']
 
    }else{
        figlet 'CI';
        if(type.contains("maven")){
             usedStages=['compile','unitTest','Jar','Sonar','nexusCI','nexusCD']
        }else{
            usedStages=['buildAndTest','sonar','runJar','rest','nexusCI']
        }
    }

return usedStages
}

return this