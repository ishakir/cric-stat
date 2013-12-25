require 'logger'
require File.expand_path("../test/config", __FILE__)
require File.expand_path('../test/lib/server', __FILE__)

# Create the logger
TestConfig::get_logger.info("About to create TestServer on port 9001")

# Grab all the files in the scripts directory
test_files = Dir[]

Dir.foreach("test/scripts") do |file|
  
  if(!File.directory?(file))
    TestServer::start_and_wait(9001)
    require File.expand_path("../test/scripts/#{file}", __FILE__)
    TestServer::end
  end

end
# For now simply start a play server
