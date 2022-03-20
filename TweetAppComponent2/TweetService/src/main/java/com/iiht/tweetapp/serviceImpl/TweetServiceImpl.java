package com.iiht.tweetapp.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.iiht.tweetapp.config.AppConfigs;
import com.iiht.tweetapp.exception.TweetNotFoundException;
import com.iiht.tweetapp.model.ResponseMessage;
import com.iiht.tweetapp.model.TweetDetails;
import com.iiht.tweetapp.repository.TweetRepository;
import com.iiht.tweetapp.service.TweetServices;

@Service
public class TweetServiceImpl implements TweetServices{
	@Autowired
	private TweetRepository tweetRepository;

	@Override
	public ResponseEntity<Object> getAllTweets() {
		List<TweetDetails> tweets=tweetRepository.findAll().stream().filter(o->o.isStatus()).collect(Collectors.toList());
		System.out.println(tweets);
		if(!tweets.isEmpty() && tweets.size()>0)
			return new ResponseEntity<Object>(tweets,HttpStatus.OK);
		return new ResponseEntity<Object>(new ResponseMessage("No Tweets Found"),HttpStatus.NO_CONTENT);
	}
	@Override
	public ResponseEntity<Object> getTweetsByUser(String username) {
		List<TweetDetails> tweets=tweetRepository.findAll().stream().filter(o->o.getUsername().equals(username)&&o.isStatus()).collect(Collectors.toList());
		if(!tweets.isEmpty()  && tweets.size()>0)
			return new ResponseEntity<Object>(tweets,HttpStatus.OK);
		return new ResponseEntity<Object>(new ResponseMessage("No Tweets Found"),HttpStatus.NO_CONTENT);
	}

	@Override
	public ResponseEntity<Object> addTweet(String username,TweetDetails tweetDetails) {
			tweetDetails.setId(Uuids.timeBased());
			tweetDetails.setUsername(username);
			tweetDetails.setStatus(true);
			tweetDetails.setTime(LocalDateTime.now());
			tweetRepository.save(tweetDetails);
			return new ResponseEntity<Object>("Added Tweet Successfully",HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<Object> updateTweet(UUID id, TweetDetails tweetDetails) throws TweetNotFoundException {
			Optional<TweetDetails> tweet=tweetRepository.findById(id);
			if(tweet.isEmpty()) {
				throw new TweetNotFoundException("Tweet not found exception");
			}
			tweet.get().setHandleName(tweetDetails.getHandleName());
			tweet.get().setMessage(tweetDetails.getMessage());
			tweet.get().setTime(LocalDateTime.now());
			tweetRepository.save(tweet.get());
			return new ResponseEntity<Object>("Updated Tweet Successfully",HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Object> deleteTweet(UUID id) throws TweetNotFoundException {
		
			Optional<TweetDetails> tweet=tweetRepository.findById(id);
			if(tweet.isEmpty()) {
				throw new TweetNotFoundException("Tweet not found exception");
			}
			tweet.get().setStatus(false);
			tweetRepository.save(tweet.get());
			return new ResponseEntity<Object>("Deleting Tweet Successfully",HttpStatus.OK);
		
	}

	@Override
	public ResponseEntity<Object> likeTweet(UUID id) throws TweetNotFoundException {
			Optional<TweetDetails> tweet=tweetRepository.findById(id);
			if(tweet.isEmpty()) {
				throw new TweetNotFoundException("Tweet not found exception");
			}
			tweet.get().setLikes(tweet.get().getLikes()+1);
			tweetRepository.save(tweet.get());
			return new ResponseEntity<Object>("liked the Tweet Successfully",HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Object> replyTweet(UUID id, String reply) throws TweetNotFoundException {
			Optional<TweetDetails> tweet=tweetRepository.findById(id);
			if(tweet.isEmpty()) {
				throw new TweetNotFoundException("Tweet not found exception");
			}
			if(tweet.get().getReplies()!=null)
				tweet.get().getReplies().add(reply);
			else {
				List<String> l=new ArrayList<String>();
				l.add(reply);
				tweet.get().setReplies(l);
			}
			tweetRepository.save(tweet.get());
			return new ResponseEntity<Object>("Replied to Tweet Successfully",HttpStatus.OK);
	}
	@KafkaListener(topics = AppConfigs.topicName,groupId = "delete")
	public ResponseEntity<Object> deleteTweetKafka(UUID id) throws TweetNotFoundException {
		Optional<TweetDetails> tweet=tweetRepository.findById(id);
		if(tweet.isEmpty()) {
			throw new TweetNotFoundException("Tweet not found exception");
		}
		tweet.get().setStatus(false);
		tweetRepository.save(tweet.get());
		return new ResponseEntity<Object>("Deleting Tweet Successfully",HttpStatus.OK);
	}

}
