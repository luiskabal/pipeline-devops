package pipeline.git

def call(){
	
}
def checkIfBranchExists(releaseBranchName){
	/*bat 'git pull'
    def output = bat (script:"@git ls-remote --heads "+releaseBranchName, returnStdout: true)
	respuesta= !output.isEmpty()?true:false
	*/
	respuesta = sh "git fetch; git ls-remote --heads origin ${releaseBranchName} | wc -l"
	return respuesta
}
def checkIfBranchUpdated(currentBranch,releaseBranchName){
	/*bat 'git checkout '+releaseBranchName
	bat 'git pull'
	def output = bat (script:"@git log origin/"+releaseBranchName+"..origin/"+currentBranch, returnStdout: true)
	respuesta= !output.isEmpty()?true:false*/
	respuesta = sh "git fetch; git checkout ${releaseBranchName}; git pull origin ${releaseBranchName} | wc -l"
	return respuesta
}
def deleteBranch(releaseBranchName){
	/*bat '''
	git pull
	git push origin --delete '''+ releaseBranchName*/
	sh "git fetch; git push origin --delete ${releaseBranchName}"
}
def createBranch(releaseBranchName,currentBranch){
	/*bat '''
	git reset --hard HEAD
	git pull
	git checkout ''' +currentBranch+ '''
	git checkout -b ''' +releaseBranchName+ '''
	git push origin ''' +releaseBranchName*/

	sh "git reset --hard HEAD; git fetch; git checkout ${currentBranch}; git checkout -b ${releaseBranchName}; git push origin ${releaseBranchName} --set-upstream"
}
def getVersion(){
	/*
	bat 'git pull'
	def version = bat(script: "@type version.txt", returnStdout: true).trim()*/
	version = sh "cat version.txt"
	return version
}
def gitDiff(branch1,branch2){
	sh "git fetch; git checkout ${branch1}; git pull origin ${branch1}; git checkout ${branch2}; git pull origin ${branch2}; git diff ${branch1}..${branch2}"
}
return this;