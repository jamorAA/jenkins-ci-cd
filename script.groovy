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
        def dockerCmd = "docker run -d -p 8080:8080 --name java-maven-app $IMAGE_NAME"
        sh "ssh -o StrictHostKeyChecking=no morant@158.160.226.219 $dockerCmd"
    }
}
return this
