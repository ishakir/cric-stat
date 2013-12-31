require 'logger'
require File.expand_path("../test/config", __FILE__)
require File.expand_path('../test/lib/server', __FILE__)

# Create the logger
TestConfig::get_logger.info("About to create TestServer on port 9001")

# Grab all the file names in the scripts directory
test_files = Dir.entries("test/scripts")

# Start the server
TestServer::start_and_wait(TestConfig::SERVER_PORT)

test_files.each do |file|
  if(!File.directory?(file))
    require File.expand_path("../test/scripts/#{file}", __FILE__)
  end
end