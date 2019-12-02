
local SENSITIVECHECKSETONE = "sen_violence_set"
local SENSITIVECHECKSETTWO = "sen_politic_set"
local SENSITIVECHECKSETTHREE = "sen_pornography_set"

for i,key in pairs(KEYS) do
	
	if( (1 == redis.call("sismember",SENSITIVECHECKSETONE, key) ) or 
	  (1 == redis.call("sismember",SENSITIVECHECKSETTWO, key)   ) or 
	  (1 == redis.call("sismember",SENSITIVECHECKSETTHREE, key))) 
	then 
	  return 1
	end
end

return 0

