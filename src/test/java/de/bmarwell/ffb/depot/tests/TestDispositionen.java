/*
 * (c) Copyright 2016 FFB Depot Client Developers.
 *
 * This file is part of FFB Depot Client.
 *
 * FFB Depot Client is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * FFB Depot Client is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with FFB Depot Client. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package de.bmarwell.ffb.depot.tests;

import de.bmarwell.ffb.depot.client.FfbMobileClient;
import de.bmarwell.ffb.depot.client.err.FfbClientError;
import de.bmarwell.ffb.depot.client.json.FfbDisposition;
import de.bmarwell.ffb.depot.client.json.FfbDispositionenResponse;
import de.bmarwell.ffb.depot.client.json.ImmutableFfbDisposition;
import de.bmarwell.ffb.depot.client.json.ImmutableFfbDispositionenResponse;

import com.google.common.collect.ImmutableList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.util.List;

public class TestDispositionen {

  /**
   * Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(TestDispositionen.class);

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testGetDispositionen() throws FfbClientError, MalformedURLException {
    FfbMobileClient mobileAgent = new FfbMobileClient(TestMobileGetDepotwert.LOGIN, TestMobileGetDepotwert.PIN);
    mobileAgent.logon();
    Assert.assertTrue(mobileAgent.loginInformation().isPresent());

    FfbDispositionenResponse umsaetze = mobileAgent.getDispositionen();
    Assert.assertNotNull(umsaetze);
    LOG.debug("Dispositionen: [{}].", umsaetze);
  }

  @Test
  public void testDispositionObjects() {
    FfbDisposition dispo1 = ImmutableFfbDisposition.of("Depot", "Fondsname", "isin", "wkn", "KAG", "Kauf", "Kauf",
        "01.01.2016",
        "ReferenzKonto", "150,00", "15,00", "50,00");
    FfbDispositionenResponse dispos = ImmutableFfbDispositionenResponse.builder()
        .isLoginAsString("true")
        .errormessage("")
        .dispositionenBetragAsString("0.00")
        .dispositionenAnzahl(100)
        .dispositionen(ImmutableList.<FfbDisposition>of(dispo1))
        .build();

    List<FfbDisposition> dispositionen = dispos.getDispositionen();
    Assert.assertFalse(dispositionen.isEmpty());
    Assert.assertEquals("Es sollte ein Element vorhanden sein.", 1, dispositionen.size());
    FfbDisposition firstDisposition = dispositionen.get(0);
    Assert.assertEquals("Das vorhandene Element sollte dem erstellten entsprechen.", dispo1, firstDisposition);
    Assert.assertTrue(firstDisposition.toString().contains("Fondsname"));
  }

}
