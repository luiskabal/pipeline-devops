package pipeline.test
import java.util.regex.Pattern

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
         usedStages=['downloadNexus','runDownload','rest','nexusCD','createRelease','gitDiff']
    }else{
        figlet 'CI';
        usedStages = type.contains('maven')?['compile','unitTest','Jar','Sonar','nexusCI']:['buildAndTest','sonar','runJar','rest','nexusCI','createRelease','gitDiff']
        if(pipelineJob.contains("develop")){
            usedStages.add('createRelease')
        }
        if(pipelineJob.contains("pipeline-cd")){
            usedStages.add('createRelease')
            usedStages.add('gitDiff')
        }
    }

return usedStages
}

def validateReleaseBranchName(releaseBranchName){
	//assert (/release-v\d+\-\d+\-\d+/) == "${releaseBranchName}"
    if ("${releaseBranchName}" =~ /release-v\d+\-\d+\-\d+/) {
        println "Rama release correctamente formada"
    }
    else {
        error "La rama release no tiene el formato correcto: ${releaseBranchName}"
    }
}

def validateFiles(choice) {
    if (choice == 'gradle') {
        def exists = fileExists('build.gradle')
        if (!exists) {
            error "Se intenta ejecutar bajo Gradle pero el directorio no tiene archivos correspondientes"
        } else {
            println "Se valido que se está ejecutando bajo gradle y los archivos necesarios están correctos"
        }
    }
    else if (choice == 'maven') {
        def exists = fileExists('pom.xml')
        if (!exists) {
            error "Se intenta ejecutar bajo Maven pero el directorio no tiene archivos correspondientes"
        } else {
            println "Se valido que se está ejecutando bajo gradle y los archivos necesarios están correctos"
        }
        
    }
}

def validateKindApp(git_url) {
    def gitRepoName = env.GIT_URL.replaceFirst(/^.*\/([^\/]+?).git$/, '$1')
    def kind = gitRepoName.split('-').toList()

    println "El tipo de aplicación es ${kind[0]}"
}

def validateVersionFile() {
    def exists = fileExists('version.txt')
        if (!exists) {
            error "No se ha agregado el archivo de versión"
        } else {
            println "Validación de existencia de archivo version.txt correcta"
        }
}

def validatePort() {
    def port = readFile "src/main/resources/application.properties"
    def number = port.split('=').toList()
    prinln "Puerto N°: ${number[1]}"
}

return this