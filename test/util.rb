require 'open3'

require_relative 'lib/api'

module Util

  module Process
    
    def Process.run_command(command, verbose = false)
      
      logger = TestConfig::get_logger
          
      if(verbose)
        logger.info("Ran command '#{command}'")
      end
      
      output = `#{command}`
      
      return ProcessResult.new(command, output, "", $?.exitstatus)
      
      # TODO do this properly, current code is causing the play start
      # process to run at 100% cpu
      #Open3.popen3(command) do |stdin, stdout, stderr, wait_thr|
      #  
      #  # We don't need stdin
      #  stdin.close
      #  
      #  # Wait for process to finish and then grab stdout, stderr
      #  out = stdout.gets
      #  err = stderr.gets
      #  
      #  stdout.close
      #  stderr.close
      #  
      #  rc = wait_thr.value.exitstatus
      #  
      #  if(verbose)
      #    logger.info("Stdout was: \n")
      #    logger.info("#{out}\n")
      #    logger.info("Stderr was: \n")
      #    logger.info("#{err}\n")
      #    logger.info("Return code was '#{rc}'")
      #  end
      #  
      #  return ProcessResult.new(command, out, err, rc)
      #  
      #end

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
  
  # As far as I'm concerned, in the test material everything is
  # done by "opposition name", which won't make sense as we scale
  # out to more than one club, but there we go
  module Data
    
    def Data.add_match(match_identifier)
      
      filename = "test/data/#{match_identifier}.csv.json"
      
      # Find file in data directory with that name
      if(!File.exists?(filename))
        raise ArgumentError.new("Match with identifier '#{match_identifier}' not found at location '#{filename}'")
      end
      
      # If it does exist upload it
      file = File.open(filename)
      file_contents = file.read
      
      response = API::Match.add_match(file_contents)
      
      if(response.code != "200")
        TestConfig::get_logger.info("ERROR: HTTP Request failed with error #{response.original_body}")
        raise "Adding match with identifier '#{match_identifier}' failed as http response code was '#{response.code}'"
      end
      
      # Return the location of the match
      response.body['link']['href']
      
    end
    
    def Data.delete_match(link)
      
      response = API::Match.delete_match(link)
        
      if(response.code != "200")
        raise 'Deleting match with link '#{link}' failed, as as http response code was '#{response.code}'""
      end
        
    end
    
    def Data.delete_all_matches(links)
      
      links.each do |link|
        
        delete_match(link)
        
      end
      
    end
    
  end

end