def increment() {
    echo "incrementing the app version..."
    sh "mvn build-helper:parse-version versions:set -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} versions:commit"
    def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
    def version = matcher[0][1]
    env.IMAGE_TAG = "$version-$BUILD_NUMBER"
    env.IMAGE_NAME = "jamoraa/demo-java-maven-app:${IMAGE_TAG}"
}

def buildJar() {
    echo "building the application..."
    sh "mvn clean package"
}

def buildImage() {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
        sh "docker build -t $IMAGE_NAME ."
        sh "echo $PASSWORD | docker login -u $USERNAME --password-stdin"
        sh "docker push $IMAGE_NAME"
    }
}

def deployApp() {
    echo "deploying the application..."
    sshagent(['ssh-key']) {
        def script = "bash ./server-commands.sh $IMAGE_NAME"
        def server = "morant@158.160.226.219"
        sh "scp server-commands.sh ${server}:/home/morant/"
        sh "scp docker-compose.yaml ${server}:/home/morant/"
        sh "ssh -o StrictHostKeyChecking=no $server $script"
    }
}

def gitPush() {
    echo "pushing version change to github repository..."
    withCredentials([string(credentialsId: 'github-token', variable: 'TOKEN')]) {
        sh "git status"
        sh "git branch"
        sh "git config --list"

        sh "git remote set-url origin https://${TOKEN}@github.com/jamorAA/jenkins-ci-cd.git"
        sh "git add pom.xml"
        sh "git commit -m 'ci: version bump'"
        sh "git push origin HEAD:main"
    }
}
return this
