require 'logger'

module TestConfig
  
  SERVER_IP = "localhost"
  SERVER_PORT = 9001
  
  LOGGER = Logger.new(STDOUT)
  LOGGER.level = Logger::DEBUG
  
  # Let's make our logger a Singleton
  def TestConfig.get_logger()
    LOGGER
  end
  
end