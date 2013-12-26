require 'net/http'

require_relative '../config'

module API
  
  BASE_URL = "http://#{TestConfig.SERVER_IP}:#{TestConfig.SERVER_PORT}/api/"
  
  module Season
    
    SEASON_URL = BASE_URL + "season" 
    
    def Season.get_response
      
      Net::HTTP.get_response(SEASON_URL)
      
    end
    
  end
  
end