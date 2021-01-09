package pipeline.git

def call(){
	
}
def checkIfBrandExists(releaseBranchName){

}
def checkIfBrandUpdated(currentBranch,releaseBranchName){

	println("BEGINS  checkIfBrandUpdated(): "+ "git log origin/"+releaseBranchName+"..origin/"+currentBranch)
	def output = bat (script:"git log origin/"+releaseBranchName+"..origin/"+currentBranch, returnStdout: true)
	println("BRAND UPDATED: "+ output)
	//respuesta= (!output?.trim())?false:true

	return output ;
}
def deleteBranch(releaseBranchName){

}
def createBranch(releaseBranchName,currentBranch){

}
return this;