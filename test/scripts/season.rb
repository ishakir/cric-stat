require 'minitest/autorun'

require_relative '../lib/api'

require_relative '../util'

describe 'the season api' do
  
  it 'should be empty when there are no matches added' do
    
    TestConfig::get_logger.info("Sending request to season api")
    response = API::Season.get_response()
    
    assert_equal(
      "200", 
      response.code,
      "ERROR: Response code is '#{response.code}', not 200"
    )
    
    response_body = response.body
    
    assert_equal(
      0,
      response_body.length,
      "ERROR: Json response is '#{response_body}' not an empty array"
    )
    
  end
  
  it 'should contain the year one match was played in' do
    
    # Add the Hambledon match to the system
    TestConfig::get_logger.info("Adding Hambledon game to the system")
    link = Util::Data.add_match("Hambledon")
    
    TestConfig::get_logger.info("Sending request to season api")
    response = API::Season.get_response()
    
    assert_equal(
      "200", 
      response.code,
      "ERROR: Response code is '#{response.code}', not 200"
    )
    
    response_body = response.body
    
    assert_equal(
      1,
      response_body.length,
      "ERROR: Json response is size '#{response_body.length}' not 1"
    )
    
    assert_equal(
      2013, 
      response_body[0],
      "ERROR: Found #{response_body[0]} from api rather than 2013!"
    )
    
    # Remove the match
    TestConfig::get_logger.info("Deleting all matches from the system")
    Util::Data.delete_match(link)
    
  end
  
  it 'should only display one year, even if two matches are added in the same year' do
    
    links = []
    
    # Add two matches from 2013
    TestConfig::get_logger.info("Adding Hambledon game to the system")
    links.push(Util::Data.add_match("Hambledon"))
    
    TestConfig::get_logger.info("Adding Trojans game to the system")
    links.push(Util::Data.add_match("Trojans"))
    
    TestConfig::get_logger.info("Sending request to season api")
    response = API::Season.get_response()
    
    assert_equal(
      "200", 
      response.code,
      "ERROR: Response code is '#{response.code}', not 200"
    )
    
    response_body = response.body
    
    assert_equal(
      1,
      response_body.length,
      "ERROR: Json response is size '#{response_body.length}' not 1"
    )
    
    assert_equal(
      2013, 
      response_body[0],
      "ERROR: Found #{response_body[0]} from api rather than 2013!"
    )
    
    # Remove the matches
    TestConfig::get_logger.info("Deleting all matches from the system")
    Util::Data.delete_all_matches(links)
    
  end
  
end
