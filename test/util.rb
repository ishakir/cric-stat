require 'open3'

module Util

  module Process
    
    def Process.run_command(command, verbose = false)
      
      logger = TestConfig::get_logger
          
      if(verbose)
        logger.info("Ran command '#{command}'")
      end
      
      Open3.popen3(command) do |stdin, stdout, stderr, wait_thr|
        
        # We don't need stdin
        stdin.close
        
        # Wait for process to finish and then grab stdout, stderr
        out = stdout.gets
        err = stderr.gets
        
        rc = wait_thr.value.exitstatus
        
        if(verbose)
          logger.info("Stdout was: \n")
          logger.info("#{out}\n")
          logger.info("Stderr was: \n")
          logger.info("#{err}\n")
          logger.info("Return code was '#{rc}")
        end
        
        return ProcessResult.new(command, out, err, rc)
        
      end

    end

  end

  class ProcessResult
    def initialize(command, stdout, stderr, rc)
      @command = command
      @stdout = stdout
      @stderr = stderr
      @rc = rc
    end

  end

end