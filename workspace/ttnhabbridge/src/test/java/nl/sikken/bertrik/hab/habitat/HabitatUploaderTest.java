package nl.sikken.bertrik.hab.habitat;

import java.util.Arrays;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import nl.sikken.bertrik.hab.Sentence;

/**
 * Unit tests for HabitatUploader.
 */
public final class HabitatUploaderTest {
    
	/**
	 * Happy flow scenario for payload upload.
	 */
	@Test
	public void testUploadPayload() {
		// create a mocked rest client
		final IHabitatRestApi restClient = Mockito.mock(IHabitatRestApi.class);
		Mockito.when(restClient.updateListener(Mockito.anyString(), Mockito.anyString())).thenReturn("OK");
		Mockito.when(restClient.getUuids(Mockito.anyInt())).thenReturn(new UuidsList(Arrays.asList("uuid1", "uuid2")));
		
		final HabitatUploader uploader = new HabitatUploader(restClient);
		
		// verify upload using the uploader
		uploader.start();
		try {
			final HabReceiver receiver = new HabReceiver("BERTRIK", null);
			final Date date = new Date();
			final Sentence sentence = new Sentence("NOTAFLIGHT", 1, date, 52.0182307, 4.695772, 1000);

			uploader.schedulePayloadTelemetryUpload(sentence.format(), Arrays.asList(receiver), date);
			Mockito.verify(restClient, Mockito.timeout(3000)).updateListener(Mockito.anyString(), Mockito.anyString());
		} finally {
			uploader.stop();
		}
	}
	
	/**
	 * Happy flow scenario for listener upload.
	 */
	@Test
	public void testUploadListener() {
        // create a mocked rest client
        final IHabitatRestApi restClient = Mockito.mock(IHabitatRestApi.class);
        Mockito.when(restClient.getUuids(Mockito.anyInt())).thenReturn(new UuidsList(Arrays.asList("uuid1", "uuid2")));
        Mockito.when(restClient.uploadDocument(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(new UploadResult(true, "id", "rev"));
        
        final HabitatUploader uploader = new HabitatUploader(restClient);
        
        // verify upload using the uploader
        uploader.start();
        try {
            final HabReceiver receiver = new HabReceiver("BERTRIK", null);
            final Date date = new Date();
            
            uploader.scheduleListenerDataUpload(receiver, date);
            Mockito.verify(restClient, Mockito.timeout(3000)).uploadDocument(Mockito.anyString(), Mockito.anyString());
        } finally {
            uploader.stop();
        }
	}

	/**
     * Verifies upload of payload telemetry to the actual habitat server on the internet.
	 * 
	 * @throws InterruptedException in case the sleep got interrupted
	 */
	@Test
    @Ignore("this is not a junit test")
	public void testActualPayloadUpload() throws InterruptedException {
		final IHabitatRestApi restClient = HabitatUploader.newRestClient("http://habitat.habhub.org", 3000);
		final HabitatUploader uploader = new HabitatUploader(restClient);
		uploader.start();
		try {
			final Date date = new Date();
			final Sentence sentence = new Sentence("NOTAFLIGHT", 1, date, 52.0182307, 4.695772, 1000);
			final HabReceiver receiver = new HabReceiver("BERTRIK", null);
			uploader.schedulePayloadTelemetryUpload(sentence.format(), Arrays.asList(receiver), date);
			Thread.sleep(3000);
		} finally {
			uploader.stop();
		}
	}
	
	/**
	 * Verifies upload of listener information and telemetry to the actual habitat server on the internet.
	 * 
     * @throws InterruptedException in case the sleep got interrupted
	 */
	@Test
    @Ignore("this is not a junit test")
	public void testActualListenerUpload() throws InterruptedException {
	    final IHabitatRestApi restClient = HabitatUploader.newRestClient("http://habitat.habhub.org", 3000);
	    final HabitatUploader uploader = new HabitatUploader(restClient);
	    try {
            final Date date = new Date();
            final HabReceiver receiver = new HabReceiver("BERTRIK", new Location(52.0182307, 4.695772, 15));
	        uploader.scheduleListenerDataUpload(receiver, date);
	        Thread.sleep(3000);
	    } finally {
	        uploader.stop();
	    }
	}
	
}
