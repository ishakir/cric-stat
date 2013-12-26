require 'logger'
require File.expand_path("../test/config", __FILE__)
require File.expand_path('../test/lib/server', __FILE__)

# Create the logger
TestConfig::get_logger.info("About to create TestServer on port 9001")

# Grab all the file names in the scripts directory
test_files = Dir.entries("test/scripts")

# If they are ruby files keep them and keep only their pre-underscore
# prefix, these are all the "files" that require testing, they will all
# have an "_0", "_1" and "_2" suffix indicating how many matches are in
# the database
# TODO if we implement match deleting functionality, then do it here
test_suites = []

test_files.select do |file|
  response = file.scan(/(\w*)_(0|1|2)\.rb/m)
  if(response.size > 0)
    suite_name = response[0][0]
    if(!test_suites.include?(suite_name))
      test_suites.push(suite_name)
    end
  end
end

require_relative 'test/scripts/season_0'

#Dir.foreach("test/scripts") do |file|
  
 # if(!File.directory?(file))
    
    # For now start the server before each test
    # (mostly as we have no delete functionality)
  #  TestServer::start_and_wait(9001)
    
   # require File.expand_path("../test/scripts/#{file}", __FILE__)
    
    # Stop the serber
    #TestServer::end
  
#  end

#end
