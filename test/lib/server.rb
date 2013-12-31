require File.expand_path("../../config", __FILE__)
require File.expand_path("../../util", __FILE__)

# For now TestServer is a singleton as we should
# only ever be running one of them
module TestServer
  
  # What's the name of that file?
  RUNNING_PID_FILE = "RUNNING_PID"

  def TestServer.start(port)
    
    # Play blocks, so start this on a new thread
    thread = Thread.new do
      TestConfig.get_logger.info("Starting server on port #{port}")
      output = Util::Process::run_command("play \"start #{port}\"", true)
    end
  
  end
  
  def TestServer.start_and_wait(port)
    start(port)
    wait_for_start()
  end
  
  def TestServer.wait_for_start
    
    attempts = 0
    
    # Wait for the running PID file to exist, probably a good indicator
    # that the server has started
    while(!File.exists?(RUNNING_PID_FILE) && attempts < 60) do
      TestConfig::get_logger.info("Waiting 1 second for RUNNING_PID file to appear")
      sleep(1)
      attempts += 1
    end
    
    if(attempts < 60)
      TestConfig::get_logger.info("Server has started")
    else
      raise 'Server failed to start!'
    end
    
    # Can take a little bit longer
    TestConfig::get_logger.info("Waiting 5 more seconds for listener to start")
    sleep(5)
    
  end
  
  def TestServer.end
    
    # Find the PID of the process
    pid = File.read(RUNNING_PID_FILE)
    TestConfig::get_logger.info("Found PID of server to be #{pid}")
    
    # Kill the process
    output = Util::Process::run_command("kill #{pid}", true)
    
  end

end