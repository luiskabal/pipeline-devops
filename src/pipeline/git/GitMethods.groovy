package pipeline.git

def call(){
	
}
def checkIfBrachExists(releaseBranchName){
	def output = bat (script:"git ls-remote --heads origin "+releaseBranchName, returnStdout: true) 
	respuesta= (!output?.trim())?true:false
	return respuesta
}
def checkIfBrachUpdated(currentBranch,releaseBranchName){

	println("BEGINS  checkIfBrachUpdated(): "+ "git log origin/"+releaseBranchName+"..origin/"+currentBranch)
	def output = bat (script:"git log origin/"+releaseBranchName+"..origin/"+currentBranch, returnStdout: true)
	println("BRAND UPDATED: "+ output)
	respuesta= (!output?.trim())?true:false

	return respuesta ;
}
def deleteBranch(releaseBranchName){

}
def createBranch(releaseBranchName,currentBranch){

}
return this;