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
return this
