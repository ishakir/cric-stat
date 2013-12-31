require 'net/http'
require 'json'

require_relative '../config'

module API
  
  BASE_URL = "http://#{TestConfig::SERVER_IP}:#{TestConfig::SERVER_PORT}/api/"
  
  class JsonHttpResponse
    
    def initialize(response)
      @response = response
    end
    
    def code
      @response.code
    end
    
    def body
      puts("Response body is '#{@response.body}'")
      JSON.parse(@response.body)
    end
    
    def original_body
      @response.body
    end
    
  end
  
  module Season
    
    SEASON_URI = URI.parse(BASE_URL + "season") 
    
    def Season.get_response
      
      TestConfig::get_logger.info("About to send request to season api")
      result = Net::HTTP.get_response(SEASON_URI)
      TestConfig::get_logger.info("Recieved response from season apip")
      JsonHttpResponse.new(result)
    
    end
    
  end
  
  module Match
    
    MATCH_URI = URI.parse(BASE_URL + "match")
    
    def Match.add_match(json)
      
      # Create the post request
      request = Net::HTTP::Post.new(MATCH_URI.request_uri)
      request.add_field('Content-Type', 'application/json')
      
      if(json.is_a? String)
        request.body = json
      else
        request.body = JSON.generate(json)
      end
      
      # Create a net HTTP, whatever that means
      http = Net::HTTP.new(MATCH_URI.host, MATCH_URI.port)
      
      TestConfig.get_logger.info("About to send new match to api")
      response = http.request(request)
      TestConfig.get_logger.info("Recieved response from match api")
      
      # Return simply the response object for now
      JsonHttpResponse.new(response)
      
    end
    
    def Match.delete_match(link)
      
      # Create the delete request
      request = Net::HTTP::Delete.new(link)
      
      # Create a net HTTP, whatever that means
      http = Net::HTTP.new(MATCH_URI.host, MATCH_URI.port)
      
      TestConfig::get_logger.info("About to send delete request to match api")
      response = http.request(request)
      TestConfig::get_logger.info("Recieved response from match api")
      
      # Return simply the response object for now
      JsonHttpResponse.new(response)
      
    end
    
  end
  
end