pipeline {
  agent any
  stages {

    stage('Build Docker image') {
      steps {
        script {
          sh "cd app"
          docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
            // Build the image using Dockerfile
            image = docker.build("felpsmac/rancher-fleet-poc:${GIT_COMMIT}")
            // Tag the image with 'latest'
            image.tag('latest')
            // Push image to the registry
            image.push()
            image.push('latest')
          }
        }
      }
    }

    // stage('Commit to github') {
    //     steps {
    //         script {
    //             // Commit the txt file to github repository
    //             git credentialsId: 'github-credentials', url: 'https://github.com/MrChampz/rancher-fleet-poc.git'
    //             sh 'git add image.txt'
    //             sh 'git commit -m "Add image.txt"'
    //             sh 'git push origin master'
    //         }
    //     }
    // }
  }
}