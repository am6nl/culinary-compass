param appName string = 'culinary-compass'
param location string = resourceGroup().location

// Parameters to control resource creation
param appServicePlanExists bool = false
param appServiceExists bool = false

// Additional parameters for Cosmos DB
param cosmosDbAccountExists bool = false
param databaseName string = 'culinary'


// Create an App Service Plan only if indicated by the parameter
resource appServicePlan 'Microsoft.Web/serverfarms@2022-09-01' = if (!appServicePlanExists) {
  name: '${appName}-plan'
  location: location
  sku: {
    name: 'F1' // Free tier
  }
  kind: 'linux'
  properties: {
    reserved: true // Required for Linux
  }
}

// Create an App Service only if indicated by the parameter
resource appService 'Microsoft.Web/sites@2022-09-01' = if (!appServiceExists) {
  name: appName
  location: location
  kind: 'app,linux'
  properties: {
    serverFarmId: appServicePlan.id
    linuxFxVersion: 'JAVA|21-java21' // Adjust for your Java version
    siteConfig: {
      appSettings: [
        {
          name: 'WEBSITE_HTTPLOGGING_RETENTION_DAYS'
          value: '7'
        }
      ]
    }
  }
  dependsOn: [
    appServicePlan
  ]
}
